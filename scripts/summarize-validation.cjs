// Consolida a execução da versão com validação, sem sobrescrever as etapas 4/5.
const fs=require('fs'),path=require('path'),crypto=require('crypto');
const root=path.resolve(__dirname,'..'),out=path.join(root,'resultados/validacao');
function reports(folder){return fs.readdirSync(folder).filter(n=>n.startsWith('TEST-')&&n.endsWith('.xml')).sort().map(name=>{
 const xml=fs.readFileSync(path.join(folder,name),'utf8');
 const attrs=xml.match(/<testsuite\s[^>]+>/)[0];
 const value=k=>Number(attrs.match(new RegExp('\\b'+k+'="([^" ]+)"'))[1]);
 return {suite:name.slice(5,-4),tests:value('tests'),failures:value('failures'),errors:value('errors'),skipped:value('skipped')};
});}
const suites=reports(path.join(out,'reports')),exhaustive=reports(path.join(out,'exhaustive-reports'));
const sum=(items,key)=>items.reduce((n,r)=>n+r[key],0);
if(sum(suites,'tests')!==181||sum(exhaustive,'tests')!==2)throw Error('Quantidade inesperada');
if([...suites,...exhaustive].some(r=>r.failures||r.errors||r.skipped))throw Error('Há falhas, erros ou testes ignorados');
const log=fs.readFileSync(path.join(out,'run.log'),'utf8');
const checks=[...log.matchAll(/^checks = (\d+)/gm)].map(m=>Number(m[1]));
const tries=[...log.matchAll(/^tries = (\d+)/gm)].map(m=>Number(m[1]));
if(checks.length!==32||checks.some(n=>n!==1000)||JSON.stringify(checks)!==JSON.stringify(tries))throw Error('Campanha PBT incompleta');
const files=['pom.xml','src/RomanNumerals.java',...fs.readdirSync(path.join(root,'tests')).filter(n=>n.endsWith('.java')).map(n=>'tests/'+n),'tests/resources/junit-platform.properties'].sort();
const data={version:'validacao-entradas-2026-10-08',checked_at:new Date().toISOString(),
 suites,standard_tests:181,failures:0,errors:0,skipped:0,valid_domain_primary_checks:30000,invalid_domain_primary_checks:2000,discarded:0,
 exhaustive_suites:exhaustive,exhaustive_directional_comparisons:7998,
 source_sha256:Object.fromEntries(files.map(file=>[file,crypto.createHash('sha256').update(fs.readFileSync(path.join(root,file))).digest('hex')])),
 note:'Resultados da validação ampliada; o experimento de quatro defeitos das etapas 4/5 pertence à versão anterior.'};
fs.writeFileSync(path.join(out,'manifest.json'),JSON.stringify(data,null,2)+'\n');
fs.writeFileSync(path.join(out,'groups.csv'),'suite,tests,failures,errors,skipped\n'+[...suites,...exhaustive].map(r=>[r.suite,r.tests,r.failures,r.errors,r.skipped].join(',')).join('\n')+'\n');
console.log('181 itens aprovados; 32000 checks primários sem descartes; 7998 comparações exaustivas aprovadas.');
