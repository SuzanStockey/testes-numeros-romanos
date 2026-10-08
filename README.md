# Testes de um conversor de números romanos

Repositório: [SuzanStockey/testes-numeros-romanos](https://github.com/SuzanStockey/testes-numeros-romanos).

Trabalho de Verificação e Validação de Software: aplicação de particionamento de equivalência, análise de valores-limite e testes baseados em propriedades ao [Roman Numerals Helper do Codewars](https://www.codewars.com/kata/51b66044bce5799a7f000003).

O sistema oferece `RomanNumerals.toRoman(int)` e `RomanNumerals.fromRoman(String)`. O contrato abrange inteiros de 1 a 3999 e suas representações romanas canônicas. O comportamento para entradas inválidas não é definido neste estudo.

## Organização

| Local | Conteúdo |
|---|---|
| `src/RomanNumerals.java` | Conversor: emissão por valores decrescentes e leitura dos valores dos símbolos |
| `tests/FixedCases.java` | 34 pares literais, com IDs de PE e AVL |
| `tests/RomanExamplesTest.java` | 68 verificações direcionais e um teste de sequência |
| `tests/RomanReference.java` | Modelo posicional e verificação independente de formato |
| `tests/RomanInfrastructureTest.java` | 35 testes de validação dos oráculos |
| `tests/RomanPropertyChecks.java` | Geradores e seis verificações de propriedades |
| `tests/Seed*Properties.java` | Execuções das seis propriedades com cinco sementes fixas |
| `tests/RomanExhaustiveTest.java` | Checagem exaustiva complementar, fora da suíte padrão |
| `especificacao.md` | Contrato, regras, requisitos e exemplos |
| `tests.md` | Técnicas, casos, geradores e rastreabilidade |
| `pesquisa/` | Fichamentos, registro do E5 e referências BibTeX |
| `resenha.tex` | Resenha crítica em LaTeX com referências IEEE incorporadas |
| `apresentacao.html` | Oito slides com navegação e visão geral; funcionamento local |
| `entrega/` | Checklist final, auditoria de integridade e pacote para revisão |
| `scripts/` | Execução por grupo e extração das evidências |
| `resultados/etapa4/` | Logs, tabelas de execução, distribuições e hashes do código avaliado |
| `resultados/etapa5/` | Experimento com quatro defeitos: variantes, relatórios, contraexemplos e comparação |

## Ambiente e dependências

Requer **JDK 17 ou posterior** e **Maven 3.9**. Foram usados JDK Microsoft 17.0.16 e Maven 3.9.11 no Windows 11. Não usar o Java 6 que está configurado como padrão nesta máquina.

Versões fixadas em `pom.xml`: JUnit Jupiter 5.13.4, jqwik 1.9.3, Maven Compiler Plugin 3.14.0 e Surefire 3.5.3. O Maven usa `/src` e `/tests`, conforme os diretórios pedidos pelo trabalho. A primeira execução pode baixar dependências do Maven Central.

No PowerShell, configure um JDK válido na sessão:

```powershell
$env:JAVA_HOME = 'C:\Users\suzan\.jdks\ms-17.0.16'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

Se `mvn` estiver no PATH, execute na raiz:

```powershell
mvn --batch-mode --no-transfer-progress test
```

A suíte padrão reúne 134 itens reportados pelo framework: 69 testes de exemplos/sequência, 35 de infraestrutura e 30 execuções de propriedades. As 30 execuções correspondem a seis propriedades com cinco sementes, cada uma com 1000 avaliações: **30.000 avaliações primárias aleatórias**. O número 134 não representa o total de entradas exercitadas.

## Executar e registrar grupos separados

Com Maven disponível no PATH:

```powershell
.\scripts\run-tests.ps1 -Group infrastructure
.\scripts\run-tests.ps1 -Group examples
.\scripts\run-tests.ps1 -Group properties
.\scripts\run-tests.ps1 -Group exhaustive
.\scripts\run-tests.ps1 -Group all
```

Na máquina deste trabalho, o Maven também está disponível pelo IntelliJ. Exemplo completo:

```powershell
.\scripts\run-tests.ps1 -Group all `
  -JavaHome 'C:\Users\suzan\.jdks\ms-17.0.16' `
  -MavenExecutable 'C:\Program Files\JetBrains\IntelliJ IDEA 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd'
```

O script grava `resultados/etapa4/<grupo>.log` e os relatórios XML/TXT em `target/reports/<grupo>`. Reexecutar um grupo substitui seu log; preserve evidências antes de modificar o código. Esses comandos destinam-se à validação da implementação base. O experimento com defeitos controlados usa `resultados/etapa5/` para preservar essa evidência.

Para executar uma semente ou propriedade específica sem sobrescrever os logs da etapa 4:

```powershell
mvn --batch-mode --no-transfer-progress test '-Dtest=Seed42Properties'
mvn --batch-mode --no-transfer-progress test '-Dtest=Seed42Properties#p01'
```

Os seis métodos `p01` a `p06` correspondem a CT-PBT-01 a CT-PBT-06. As classes são `Seed42Properties`, `Seed2024Properties`, `Seed2026Properties`, `Seed3999Properties` e `Seed104729Properties`. A semente aparece nas anotações e no relatório do jqwik; não depende de um parâmetro informal passado à JVM.

## Reproduzir a checagem exaustiva

```powershell
mvn --batch-mode --no-transfer-progress test '-Dtest=RomanExhaustiveTest' '-DexcludedGroups=none'
```

São dois testes com laços: 3999 comparações de codificação e 3999 de decodificação, totalizando 7998 comparações. Eles ficam separados da campanha aleatória e são excluídos de `mvn test` por padrão.

## Conferir as evidências

Depois de executar os cinco grupos, com Python 3 disponível:

```powershell
python scripts/summarize-results.py
```

O script confere os totais, sementes, modo aleatório, tentativas e estatísticas e gera `groups.csv`, `properties.csv`, `distributions.csv` e `manifest.json`. Os hashes SHA-256 identificam os arquivos de produção, teste e configuração presentes quando as evidências foram extraídas; não substituem histórico de versionamento.

Os logs confirmam zero falhas, erros e descartes na campanha base. As propriedades usam uma mistura 80/20 de inteiros uniformes e valores-limite, com injeção automática de casos especiais desabilitada. O banco de reexecução de falhas do jqwik também está desabilitado para evitar interferência de campanhas anteriores. As mesmas sementes e o mesmo gerador reutilizam sequências entre propriedades; 30.000 avaliações não são 30.000 entradas independentes ou distintas.

O resumo da implementação base está em [resultados/etapa4/resumo.md](resultados/etapa4/resumo.md). O [experimento da etapa 5](resultados/etapa5/resumo.md) comparou quatro variantes isoladas: exemplos, PBT e combinação detectaram 4/4. O conversor correto foi restaurado e os 134 itens passaram novamente. Esses defeitos artificiais não permitem concluir superioridade de uma técnica.

## Experimento com defeitos controlados

Com Python 3, JDK 17 e Maven disponíveis:

```powershell
python scripts/run-experiment.py --java-home $env:JAVA_HOME --maven mvn
python scripts/summarize-experiment.py
```

O parâmetro `--maven` também aceita o caminho completo do `mvn.cmd` mostrado acima. O primeiro script exige os hashes da etapa 4 intactos, restaura o código em bloco `finally` e valida a suíte correta ao terminar. Recusa sobrescrever a evidência existente: preserve `resultados/etapa5/` antes de repetir. Falhas de asserção e saída Maven 1 são esperadas nas variantes. O segundo script apenas lê as evidências, confere a integridade e regenera os CSV. Contraexemplos e limites da comparação estão no resumo da etapa 5.

## Uso de IA e participação no trabalho

O trabalho foi desenvolvido com diretrizes, acompanhamento e revisão do estudante em cada etapa. O estudante escolheu inicialmente o artigo de 2024, definiu os requisitos e formatos dos entregáveis e avaliou as propostas, solicitando ajustes ao longo do processo. A IA foi utilizada como ferramenta de colaboração na pesquisa e análise dos artigos, nas propostas de problema e técnicas, na elaboração de código e testes, na execução e análise dos experimentos e na preparação e revisão dos textos e da apresentação. As propostas e os materiais gerados foram acompanhados e revisados pelo estudante, que permanece responsável pelas decisões adotadas e pela entrega.

## Estado do trabalho

Etapas 1 a 5 concluídas. A resenha da etapa 6 está escrita em `resenha.tex` e aberta no editor LaTeX. A compilação integrada falhou por problema do ambiente; PDF, paginação e revisão visual ainda não foram validados. A autoria precisa receber nome e matrícula. O registro de revisão está em `pesquisa/revisao-resenha.md`. A etapa 7 tem oito slides em `apresentacao.html` para uma fala planejada de seis minutos. O roteiro de fala é um arquivo TXT de uso local, excluído do versionamento. A revisão de entrega está registrada abaixo. Os resultados são da suíte local, sem submissão ao Codewars. O projeto está publicado no repositório GitHub indicado no início deste documento.

A revisão da etapa 8 foi concluída: 41 verificações aprovadas, com código restaurado, resultados coerentes e apresentação conferida. O checklist e as pendências estão em [entrega/LEIA-ME.md](entrega/LEIA-ME.md). O pacote ZIP é para revisão, pois PDF, autoria e ensaio ainda precisam ser finalizados. Para auditar novamente os arquivos e evidências sem reexecutar testes: `node scripts/check-delivery.cjs` (Node.js 18 ou posterior).
