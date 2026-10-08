# Revisão final — etapa 8

## Resultado da revisão

A revisão geral posterior está em `revisao-final.md`, incluindo correções conceituais, atualização de estados e redução de metadados locais nos XML, sem alteração dos casos de teste.

Foram aprovadas 41 verificações de arquivos, hashes, detecção de defeitos, links locais da apresentação e chaves de citações LaTeX. O registro verificável está em `verificacao.json`; para repetir a auditoria, execute `node scripts/check-delivery.cjs` na raiz do projeto. A suíte Java não foi reexecutada nesta etapa porque seus 14 arquivos de configuração/produção/testes mantêm os hashes da etapa 4. A execução após restauração, preservada na etapa 5, registra 134 itens aprovados.

A apresentação foi conferida em Microsoft Edge: oito slides, navegação, visão geral e largura móvel; os slides não transbordam a área de apresentação em 1366 × 768. O plano de fala soma seis minutos; é necessário ensaio para confirmar a duração real.

## Checklist do enunciado

| Requisito | Estado | Evidência |
|---|---|---|
| 2–3 artigos acadêmicos citados e resenhados | Conteúdo pronto | `resenha.tex`, `pesquisa/fichamentos.md` |
| Segundo artigo conforme E5 | Pronto | `pesquisa/pesquisa-e5.md`: expressão, motivo e URL |
| Kata disponível em site de desafios | Pronto | Roman Numerals Helper, Codewars; URL no README |
| Linguagem e plataforma identificadas | Pronto | Java 17, JUnit, jqwik e Maven |
| Ao menos duas técnicas aplicadas | Pronto | PE, AVL e PBT em `tests.md` e `/tests` |
| Código em `/src` | Pronto | `src/RomanNumerals.java` |
| Código de testes em `/tests` | Pronto | Testes JUnit e propriedades jqwik |
| Casos claros e rastreabilidade | Pronto | `tests.md`, `especificacao.md` |
| Resultados e ameaças à validade | Pronto | Resenha e `resultados/etapa5/resumo.md` |
| README com reprodução e participação | Pronto | `README.md` |
| Resenha PDF de 2–4 páginas | **Pendente** | Fonte pronta; PDF e paginação não validados |
| Referências ABNT ou IEEE | Estrutura IEEE pronta | Compilação necessária para validar apresentação final |
| Repositório GitHub | Publicado | https://github.com/SuzanStockey/testes-numeros-romanos |
| Apresentação de 5–7 minutos | Material pronto; ensaio pendente | HTML e roteiro local de seis minutos |
| Identificação do estudante | **Pendente** | Nome e matrícula não informados |
| Trabalho individual | Declarado | Confirmar com o professor, pois o enunciado prevê duplas/trios |

## O que falta para submeter

1. Inserir nome e matrícula no `\author` de `resenha.tex` e na capa do HTML.
2. Compilar a mesma fonte LaTeX, revisar o PDF visualmente e confirmar 2–4 páginas. A nova tentativa nesta etapa retornou novamente `Unable to find standard directories for platform`. Nenhum PDF foi produzido; três quebras manuais não garantem quatro páginas se houver transbordamento.
3. Usar o repositório publicado: https://github.com/SuzanStockey/testes-numeros-romanos. O envio da branch `main` foi confirmado por comparação entre a revisão local e a remota.
4. Ensaiar a fala usando o roteiro e ajustar ao intervalo de 5–7 minutos.
5. Confirmar com o professor a entrega individual. A ausência da dupla foi informada pelo estudante; não se deve atribuir participação ao integrante ausente.
6. Enviar o PDF final e o link do repositório pelo canal da disciplina, com a apresentação conforme solicitado.

## Pacote para revisão

`trabalho-revisao.zip` reúne fontes, testes, configuração Maven, pesquisa, apresentação, scripts e evidências. É um pacote de revisão, **não uma entrega final pronta**: não contém o PDF obrigatório. A versão atual dos arquivos está no repositório GitHub acima. Os PDFs de leitura e do enunciado permanecem na pasta de trabalho e não são incluídos no ZIP; suas referências e links estão na pesquisa. Os diretórios de compilação e temporários, arquivos `.class` e o próprio ZIP também são excluídos.

O LaTeX é autossuficiente, com bibliografia incorporada. O HTML funciona localmente sem bibliotecas externas; o roteiro de fala está somente no TXT local e não integra a apresentação publicada. Sistema, testes e sonda são Java; scripts auxiliares PowerShell/Python/Node automatizam execução e verificação. Não houve substituição dos testes Java por scripts.

**Conclusão da etapa 8:** revisão técnica e organização concluídas; submissão final ainda depende das pendências acima.
