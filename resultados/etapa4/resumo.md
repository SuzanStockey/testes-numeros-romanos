# Resultados da etapa 4 - implementação base

## Ambiente e identificação

Execuções realizadas em **07/10/2026**, entre 21:42:04 e 21:42:15, horário de São Paulo (UTC-03:00), conforme os registros do Maven.

- Windows 11, arquitetura amd64.
- JDK Microsoft OpenJDK 17.0.16.
- Maven 3.9.11.
- JUnit Jupiter 5.13.4 e jqwik 1.9.3.
- Maven Compiler Plugin 3.14.0 e Surefire 3.5.3.
- Implementação base, sem os quatro defeitos controlados da etapa 5.

[manifest.json](manifest.json) registra os hashes SHA-256 dos arquivos avaliados. As evidências foram extraídas dos logs por `scripts/summarize-results.py`, e seus totais, sementes e modos de geração foram conferidos.

## Execuções e resultados

| Grupo | Itens reportados pelo framework | Trabalho realizado | Falhas/erros/ignorados | Tempo total Maven |
|---|---:|---|---|---|
| Infraestrutura | 35 | Modelo posicional conferido em 34 pares literais; um teste do oráculo de formato | 0 / 0 / 0 | 1,426 s |
| Exemplos | 69 | 68 verificações direcionais de PE/AVL e uma sequência de seis chamadas | 0 / 0 / 0 | 1,425 s |
| Propriedades | 30 | Seis propriedades com cinco sementes, 1000 avaliações por execução | 0 / 0 / 0 | 1,909 s |
| Exaustivo complementar | 2 | Dois laços, totalizando 7998 comparações direcionais | 0 / 0 / 0 | 1,337 s |
| Suíte integrada padrão | 134 | Infraestrutura + exemplos + propriedades; exclui a checagem exaustiva | 0 / 0 / 0 | 2,123 s |

Os tempos incluem preparação do Maven e não constituem benchmark nem medida isolada de custo da técnica. As dependências já estavam no cache nessas execuções.

A suíte integrada repetiu as verificações dos grupos anteriores. Seus 30.000 checks de propriedades não são evidência de mais 30.000 entradas distintas ou independentes. O teste exaustivo tem dois itens no relatório porque cada método percorre 3999 valores.

## Campanha de propriedades

Os 30 relatórios confirmam:

- `generation = RANDOMIZED`.
- `tries = 1000` e `checks = 1000` em cada execução.
- Sementes `42`, `2024`, `2026`, `3999` e `104729`.
- **30.000 avaliações primárias, zero descartes e zero falhas.**
- Injeção automática de casos especiais desabilitada na anotação e nos geradores; distribuição uniforme explicitamente configurada no ramo geral.
- Gerador composto com peso 8 para inteiros uniformes e peso 2 para os 25 valores-limite.
- Banco de reexecução de falhas desabilitado; nenhuma redução de contraexemplo foi necessária na implementação base.

As seis propriedades reutilizam a mesma geração com a mesma semente, incluindo o mapeamento para pares canônicos. As distribuições observadas coincidem entre as seis propriedades de cada semente. Há 5000 posições de geração entre as cinco sementes, reutilizadas em seis verificações; essas posições também podem repetir valores. Não foi medida a quantidade de valores distintos.

### Distribuição observada

Contagens de CT-PBT-01, representativas das demais propriedades de cada semente:

| Semente | Sem subtração CE-S0 | Uma posição CE-S1 | Duas/três CE-S2 | 1..9 | 10..99 | 100..999 | 1000..3999 |
|---:|---:|---:|---:|---:|---:|---:|---:|
| 42 | 468 | 410 | 122 | 57 | 73 | 248 | 622 |
| 2024 | 454 | 411 | 135 | 63 | 70 | 236 | 631 |
| 2026 | 492 | 379 | 129 | 58 | 79 | 244 | 619 |
| 3999 | 485 | 399 | 116 | 53 | 77 | 230 | 640 |
| 104729 | 477 | 405 | 118 | 58 | 83 | 250 | 609 |

Todas as categorias planejadas apareceram. Os seis centros subtrativos (4, 9, 40, 90, 400, 900) e os extremos 1 e 3999 também apareceram em cada execução. Isso é uma observação desta campanha, não uma garantia geral do gerador. As frequências não devem ser confundidas com cobertura de código ou taxas de detecção de defeitos.

## Evidências disponíveis

- [groups.csv](groups.csv): totais e horários por grupo.
- [properties.csv](properties.csv): propriedade, semente, tentativas, checks, descartes e modo efetivo.
- [distributions.csv](distributions.csv): contagens das três estatísticas em cada execução.
- Logs: [infrastructure.log](infrastructure.log), [examples.log](examples.log), [properties.log](properties.log), [exhaustive.log](exhaustive.log) e [all.log](all.log).
- Relatórios XML/TXT locais: `target/reports/<grupo>`, regeneráveis pelos comandos do README.

## Interpretação e limites

A implementação atendeu aos casos fixos e às propriedades verificadas. A checagem exaustiva confirmou as duas operações em todo o domínio, tomando o modelo posicional como referência. A validade dessa conclusão depende do oráculo: o modelo foi conferido com exemplos e possui uma estratégia distinta da produção, mas ainda pode compartilhar erros de interpretação da especificação.

O oráculo de formato é testado com entradas inválidas apenas como infraestrutura. Isso não cria exigências de rejeição dessas entradas pelo conversor.

CT-PBT-02 recebeu uma guarda adicional para conferir que o resultado intermediário do decodificador pertence ao domínio antes de chamar o codificador, simetricamente à guarda de formato em CT-PBT-01. Isso preserva as pré-condições em caso de falha de uma função.

Não houve submissão ao Codewars. Também não foram introduzidos defeitos nesta etapa; portanto, ainda não se pode comparar a eficácia de detecção das técnicas. O próximo experimento deverá manter esta implementação base preservada e registrar cada variante separadamente.
