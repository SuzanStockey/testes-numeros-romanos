param(
    [ValidateSet('all', 'examples', 'properties', 'infrastructure', 'exhaustive')]
    [string]$Group = 'all',
    [string]$MavenExecutable = 'mvn',
    [string]$JavaHome = $env:JAVA_HOME
)
$ErrorActionPreference = 'Stop'
$OutputEncoding = [System.Text.UTF8Encoding]::new($false)
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskManifest = Get-Content -LiteralPath (Join-Path $taskRoot 'resultados/etapa4/manifest.json') -Raw | ConvertFrom-Json
foreach ($taskProperty in $taskManifest.source_sha256.PSObject.Properties) {
    $taskDigest = (Get-FileHash -LiteralPath (Join-Path $taskRoot $taskProperty.Name) -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($taskDigest -ne $taskProperty.Value) {
        throw 'Este runner preserva a campanha original. Use a revisão 78399cf em uma cópia separada; a versão atual registra resultados em resultados/validacao.'
    }
}
if ($JavaHome) { $env:JAVA_HOME = $JavaHome }
$selections = @{
    examples = 'RomanExamplesTest'
    properties = 'Seed*Properties'
    infrastructure = 'RomanInfrastructureTest'
    exhaustive = 'RomanExhaustiveTest'
}
$evidenceDir = Join-Path $taskRoot 'resultados/etapa4'
New-Item -ItemType Directory -Path $evidenceDir -Force | Out-Null
$mavenArgs = @('--batch-mode', '--no-transfer-progress', 'test', "-Dtest.reports=target/reports/$Group")
if ($Group -ne 'all') { $mavenArgs += "-Dtest=$($selections[$Group])" }
if ($Group -eq 'exhaustive') { $mavenArgs += '-DexcludedGroups=none' }
Push-Location -LiteralPath $taskRoot
try {
    & $MavenExecutable @mavenArgs 2>&1 | Tee-Object -FilePath (Join-Path $evidenceDir "$Group.log")
    $runExitCode = $LASTEXITCODE
    if ($runExitCode -ne 0) { throw "Maven terminou com código $runExitCode. Consulte resultados/etapa4/$Group.log." }
} finally {
    Pop-Location
}
