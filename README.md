# Testes de um conversor de números romanos

**Autora:** Suzan Stockey Pereira. Trabalho de Verificação e Validação de Software.

Aplicação de particionamento de equivalência, análise de valores-limite e testes baseados em propriedades ao [Roman Numerals Helper do Codewars](https://www.codewars.com/kata/51b66044bce5799a7f000003).

O conversor implementa `toRoman(int)` e `fromRoman(String)` para inteiros de 1 a 3999 e romanos canônicos. Entradas inválidas são rejeitadas com `IllegalArgumentException`, sem normalização, conforme a política do projeto.

## Arquivos da entrega

| Arquivo ou pasta | Conteúdo |
|---|---|
| `src/` | Conversor em Java |
| `tests/` | Exemplos, propriedades, modelo independente e checagem exaustiva |
| `pom.xml` | Dependências e configuração Maven |
| `especificacao.md` | Requisitos, regras e exemplos |
| `tests.md` | Casos de teste, técnicas e rastreabilidade |
| `resenha.tex` e `resenha.pdf` | Relatório em LaTeX e PDF verificado com quatro páginas |
| `apresentacao.html` | Apresentação em HTML |
| `resultados/` | Evidências de execução, tabelas, variantes e contraexemplos |

## Artigos utilizados e registro do E5

| Artigo | Autores | Aplicação no trabalho |
|---|---|---|
| [The Category-Partition Method for Specifying and Generating Functional Tests (1988)](https://doi.org/10.1145/62959.62964) | Ostrand e Balcer | Organização dos casos por categorias e escolhas |
| [QuickCheck: A Lightweight Tool for Random Testing of Haskell Programs (2000)](https://doi.org/10.1145/351240.351266) — **artigo escolhido para o E5** | Claessen e Hughes | Propriedades executáveis e geração de entradas |
| [Property-Based Testing in Practice (2024)](https://doi.org/10.1145/3597503.3639581) | Goldstein et al. | Benefícios, dificuldades e avaliação dos testes por propriedades |

**Expressão de busca do E5:** `QuickCheck lightweight tool random testing Haskell Claessen Hughes 2000 pdf Chalmers`.

**Motivo da escolha:** QuickCheck apresenta o uso de propriedades executáveis e geração automática de entradas no teste de software. Discute pré-condições, geradores e distribuição dos dados, oferecendo fundamentos para testar o conversor. Complementa o artigo de 2024, que analisa benefícios e dificuldades de PBT na prática.

**URL do texto escolhido:** [PDF de QuickCheck](https://www.cs.tufts.edu/~nr/cs257/archive/john-hughes/quick.pdf).

## Executar os testes

Requer JDK 17 e Maven 3.9. Dependências fixadas: JUnit Jupiter 5.13.4 e jqwik 1.9.3. Execute na raiz:

```shell
mvn --batch-mode --no-transfer-progress test
```

A suíte padrão aprovou 181 itens: 69 exemplos/sequência, 35 de infraestrutura, 30 execuções de propriedades de conversão, 45 casos de validação e duas execuções de propriedades de rejeição. Foram 32.000 avaliações primárias; elas não representam entradas distintas.

Para executar apenas uma propriedade ou a checagem exaustiva complementar:

```shell
mvn --batch-mode --no-transfer-progress test '-Dtest=Seed42Properties#p01'
mvn --batch-mode --no-transfer-progress test '-Dtest=RomanExhaustiveTest' '-DexcludedGroups=none'
```

A checagem exaustiva verifica 3999 pares nos dois sentidos, totalizando 7998 comparações. Ela fica fora da suíte padrão. Os arquivos `Seed*Properties.java` registram as cinco sementes usadas.

## Resultados

- [Campanha base](resultados/etapa4/resumo.md): 134 itens aprovados e 30.000 avaliações de propriedades.
- [Experimento com defeitos](resultados/etapa5/resumo.md): exemplos, propriedades e combinação detectaram os quatro defeitos.
- [Validação de entradas](resultados/validacao/resumo.md): 181 itens aprovados e 32.000 avaliações.

A matriz de defeitos refere-se à revisão `78399cf`, cujo conversor está preservado em `resultados/etapa5/baseline/RomanNumerals.java`. Cada variante tem código e diferença registrados; a campanha não foi repetida na implementação com rejeição de entradas. Para reproduzi-la, use uma cópia separada dessa revisão e aplique uma variante por vez, mantendo os testes iguais.

Os registros correspondem a execuções locais, sem submissão ao Codewars. Os resultados não estabelecem superioridade geral de uma técnica.

## Relatório e apresentação

Compile `resenha.tex` em um ambiente LaTeX. Abra `apresentacao.html` no navegador: setas para navegar, F para tela cheia e O para visão geral. O roteiro de fala e os materiais internos de apoio ficam apenas na cópia local.

## Uso de IA e participação no trabalho

A IA foi utilizada, sob diretrizes, acompanhamento e revisão da autora, como apoio à pesquisa e análise dos artigos, à elaboração de código e testes e à execução dos experimentos. Também auxiliou na revisão e refatoração do projeto, na inclusão de casos de entradas inválidas e valores-limite e nos ajustes da documentação. A autora revisou as alterações e permanece responsável pelas decisões e pela entrega.
