// Auditoria de arquivos e evidências existentes; não reexecuta testes.
const fs=require('fs');const path=require('path');const crypto=require('crypto');
const root=path.resolve(__dirname,'..');const read=p=>fs.readFileSync(path.join(root,p),'utf8');
const sha=p=>crypto.createHash('sha256').update(fs.readFileSync(path.join(root,p))).digest('hex');
const manifest=JSON.parse(read('resultados/etapa4/manifest.json'));
const experiment=JSON.parse(read('resultados/etapa5/experiment.json'));
const checks=[];const check=(name,ok,detail)=>{checks.push({name,status:ok?'approved':'failed',detail});if(!ok)throw Error(name+': '+detail)};
for(const [file,digest] of Object.entries(manifest.source_sha256))check('hash '+file,sha(file)===digest,'Igual à implementação avaliada na etapa 4.');
for(const file of ['src/RomanNumerals.java','tests.md','README.md','especificacao.md','resenha.tex','apresentacao.html','roteiro-apresentacao.md','pesquisa/pesquisa-e5.md','pesquisa/fichamentos.md'])check('arquivo '+file,fs.statSync(path.join(root,file)).size>0,'Presente e não vazio.');
const restored=experiment.runs.find(r=>r.group==='restored');
check('restauração',experiment.restored_byte_for_byte&&restored.tests===134&&restored.failures===0,'134 itens aprovados no relatório existente; código preservado.');
for(const variant of Object.keys(experiment.variant_sha256)){
 for(const group of ['examples','properties','combined']){
  const run=experiment.runs.find(r=>r.variant===variant&&r.group===group);
  check(variant+'/'+group,run.failures>0&&run.cases.every(c=>['passed','failure'].includes(c.status)),'Detecção funcional, sem erros ou testes ignorados.');
 }
}
const html=read('apresentacao.html');
const timings=[...html.matchAll(/data-seconds="(\d+)"/g)].map(m=>Number(m[1]));
check('slides e tempo',timings.length===8&&timings.reduce((a,b)=>a+b,0)===360,'8 slides; 360 segundos planejados, sem medição da fala.');
for(const [,href] of html.matchAll(/href="([^"]+)"/g))if(!/^(https?:|#)/.test(href))check('link HTML '+href,fs.existsSync(path.join(root,href)),'Destino local existente.');
const tex=read('resenha.tex');const cites=[...tex.matchAll(/\\cite\{([^}]+)\}/g)].flatMap(m=>m[1].split(','));
const bib=[...tex.matchAll(/\\bibitem\{([^}]+)\}/g)].map(m=>m[1]);
check('citações LaTeX',cites.every(c=>bib.includes(c))&&bib.includes('quickcheck')&&bib.includes('practice'),'Chaves e duas referências acadêmicas presentes; não substitui compilação.');
const out={checked_at:new Date().toISOString(),kind:'static-delivery-audit',checks,
 pending:['Nome e matrícula não informados.','PDF não gerado: compilador integrado indisponível; limite de 2–4 páginas sem validação.','Repositório GitHub ainda sem URL/publicação.','Ensaio de 5–7 minutos ainda não realizado pelo estudante.'],
 note:'Não houve reexecução da suíte: os hashes confirmam o mesmo código dos resultados aprovados.'};
fs.mkdirSync(path.join(root,'entrega'),{recursive:true});fs.writeFileSync(path.join(root,'entrega/verificacao.json'),JSON.stringify(out,null,2)+'\n');
console.log(checks.length+' verificações aprovadas. Pendências: '+out.pending.length+'.');
