"""Executa as quatro variantes isoladas, preservando a implementação e a etapa 4.

Uso: python scripts/run-experiment.py --maven CAMINHO_MVN --java-home CAMINHO_JDK
Somente biblioteca padrão. Falhas de teste são esperadas nas variantes; falhas
de compilação ou restauração interrompem a campanha.
"""
import argparse
import difflib
import hashlib
import json
import os
from pathlib import Path
import subprocess
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parent.parent
SOURCE = ROOT / "src/RomanNumerals.java"
OUT = ROOT / "resultados/etapa5"
PROBE = r'''public class ExperimentProbe {
    public static void main(String[] args) {
        String id = args[0];
        int wrongEncoding = 0, wrongDecoding = 0;
        for (int n = 1; n <= 3999; n++) {
            String expected = RomanReference.encode(n);
            if (!expected.equals(RomanNumerals.toRoman(n))) wrongEncoding++;
            if (RomanNumerals.fromRoman(expected) != n) wrongDecoding++;
        }
        String witness = switch (id) {
            case "DF-01" -> RomanNumerals.toRoman(4);
            case "DF-02" -> Integer.toString(RomanNumerals.fromRoman("IV"));
            case "DF-03" -> RomanNumerals.toRoman(1001);
            case "DF-04" -> RomanNumerals.toRoman(3999);
            default -> "baseline";
        };
        System.out.printf("{\"encoding_differences\":%d,\"decoding_differences\":%d,\"witness_observed\":\"%s\"}%n",
            wrongEncoding, wrongDecoding, witness);
    }
}
'''


def sha(data):
    return hashlib.sha256(data).hexdigest()


def replace_once(source, old, new):
    if source.count(old) != 1:
        raise ValueError(f"Ponto de alteração não é único: {old}")
    return source.replace(old, new, 1)


def read_reports(folder):
    cases = []
    for path in sorted(folder.glob("TEST-*.xml")):
        suite = ET.parse(path).getroot()
        for case in suite.findall("testcase"):
            failure = case.find("failure")
            error = case.find("error")
            skipped = case.find("skipped")
            item = {
                "class": case.attrib.get("classname"), "name": case.attrib.get("name"),
                "time": case.attrib.get("time"),
                "status": "failure" if failure is not None else "error" if error is not None
                          else "skipped" if skipped is not None else "passed",
            }
            if failure is not None:
                item["message"] = failure.attrib.get("message", "")
                item["trace"] = failure.text or ""
            if error is not None:
                item["message"] = error.attrib.get("message", "")
            cases.append(item)
    if not cases:
        raise RuntimeError(f"Nenhum teste executado em {folder}")
    return cases


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--maven", default="mvn")
    parser.add_argument("--java-home", required=True)
    args = parser.parse_args()
    baseline = SOURCE.read_bytes()
    reference_manifest = json.loads((ROOT / "resultados/etapa4/manifest.json").read_text(encoding="utf-8"))
    # O experimento somente começa com todos os arquivos avaliados na etapa 4 intactos.
    for path, digest in reference_manifest["source_sha256"].items():
        if sha((ROOT / path).read_bytes()) != digest:
            raise RuntimeError(f"Arquivo difere da etapa 4: {path}")
    original = baseline.decode("utf-8")
    variants = {
        "DF-01": replace_once(original, '"V", "IV", "I"', '"V", "IIII", "I"'),
        "DF-02": replace_once(original, "result += current < next ? -current : current;", "result += current;"),
        "DF-03": replace_once(original, "int remaining = n;",
            "int remaining = n >= 100 && (n / 10 % 10 == 0 || n / 100 % 10 == 0) ? n - n % 10 : n;"),
        "DF-04": replace_once(original, "int remaining = n;", "int remaining = n == 3999 ? 3998 : n;"),
    }
    OUT.mkdir(parents=True, exist_ok=True)
    if (OUT / "experiment.json").exists():
        raise RuntimeError("Evidência já existe. Preserve-a antes de repetir o experimento.")
    baseline_copy = OUT / "baseline/RomanNumerals.java"
    baseline_copy.parent.mkdir(parents=True, exist_ok=True)
    baseline_copy.write_bytes(baseline)
    for identifier, content in variants.items():
        folder = OUT / identifier
        folder.mkdir(parents=True, exist_ok=True)
        (folder / "RomanNumerals.java").write_text(content, encoding="utf-8")
        patch = difflib.unified_diff(original.splitlines(keepends=True), content.splitlines(keepends=True),
                                     fromfile="baseline/RomanNumerals.java", tofile=f"{identifier}/RomanNumerals.java")
        (folder / "change.diff").write_text("".join(patch), encoding="utf-8")
    probe_folder = OUT / "probe"
    probe_folder.mkdir(exist_ok=True)
    (probe_folder / "ExperimentProbe.java").write_text(PROBE, encoding="utf-8")
    env = os.environ.copy()
    env["JAVA_HOME"] = args.java_home
    env["MAVEN_OPTS"] = env.get("MAVEN_OPTS", "") + " -Dfile.encoding=UTF-8"
    java = str(Path(args.java_home) / "bin/java.exe")
    javac = str(Path(args.java_home) / "bin/javac.exe")
    classpath = os.pathsep.join(str(ROOT / p) for p in ("target/classes", "target/test-classes", "resultados/etapa5/probe"))
    runs = []
    validation = {}
    commands = []

    def execute(identifier, group, selector):
        folder = OUT / identifier / group
        folder.mkdir(parents=True, exist_ok=True)
        reports = folder / "reports"
        if list(reports.glob("TEST-*.xml")):
            raise RuntimeError("Relatórios antigos no destino; execução não iniciada")
        command = [args.maven, "--batch-mode", "--no-transfer-progress", "test",
                   f"-Dtest={selector}", f"-Dtest.reports={reports}"]
        commands.append({"variant": identifier, "group": group, "arguments": command})
        with (folder / "run.log").open("wb") as log:
            process = subprocess.run(command, cwd=ROOT, env=env, stdout=log, stderr=subprocess.STDOUT)
        cases = read_reports(reports)
        expected_count = {"examples": 69, "properties": 30, "combined": 99, "restored": 134}[group]
        if len(cases) != expected_count or any(c["status"] in ("error", "skipped") for c in cases):
            raise RuntimeError(f"Execução incompleta ou erro não funcional: {identifier}/{group}")
        failed = sum(c["status"] == "failure" for c in cases)
        if process.returncode not in (0, 1) or (failed == 0) != (process.returncode == 0):
            raise RuntimeError(f"Saída Maven incompatível com o resultado dos testes: {identifier}/{group}")
        record = {"variant": identifier, "group": group, "exit_code": process.returncode,
                  "tests": len(cases), "failures": failed, "cases": cases}
        runs.append(record)
        print(f"{identifier}/{group}: {len(cases)} itens, {failed} falhas de asserção", flush=True)

    try:
        for identifier, content in variants.items():
            # Cada variante deriva dos mesmos bytes; restaurar entre variantes.
            SOURCE.write_bytes(baseline)
            SOURCE.write_bytes(content.encode("utf-8"))
            execute(identifier, "examples", "RomanExamplesTest")
            subprocess.run([javac, "-encoding", "UTF-8", "-cp", classpath,
                            str(probe_folder / "ExperimentProbe.java")], cwd=ROOT, env=env, check=True)
            result = subprocess.run([java, "-cp", classpath, "ExperimentProbe", identifier],
                                    cwd=ROOT, env=env, check=True, capture_output=True, text=True, encoding="utf-8")
            validation[identifier] = json.loads(result.stdout)
            if validation[identifier]["encoding_differences"] + validation[identifier]["decoding_differences"] == 0:
                raise RuntimeError("Variante não altera comportamento válido")
            (OUT / identifier / "validation.json").write_text(json.dumps(validation[identifier], indent=2) + "\n", encoding="utf-8")
            execute(identifier, "properties", "Seed*Properties")
            execute(identifier, "combined", "RomanExamplesTest,Seed*Properties")
    finally:
        SOURCE.write_bytes(baseline)
        if sha(SOURCE.read_bytes()) != sha(baseline):
            raise RuntimeError("Falha ao restaurar a implementação base")
        print("Código de produção restaurado byte a byte.", flush=True)
    # Recompilar os bytes corretos e verificar todos os testes padrão após a restauração.
    execute("baseline", "restored", "RomanExamplesTest,RomanInfrastructureTest,Seed*Properties")
    if runs[-1]["failures"]:
        raise RuntimeError("A suíte restaurada apresentou falhas")
    if any(sha((ROOT / p).read_bytes()) != digest for p, digest in reference_manifest["source_sha256"].items()):
        raise RuntimeError("Arquivos da etapa 4 foram alterados durante o experimento")
    metadata = {"baseline_sha256": sha(baseline),
                "variant_sha256": {k: sha(v.encode("utf-8")) for k, v in variants.items()},
                "java_home": args.java_home, "commands": commands,
                "validation": validation, "runs": runs,
                "restored_byte_for_byte": True,
                "note": "Diferenças exaustivas validam os defeitos; não são contabilizadas na detecção dos grupos."}
    (OUT / "experiment.json").write_text(json.dumps(metadata, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("Experimento concluído e suíte base aprovada.", flush=True)


if __name__ == "__main__":
    main()
