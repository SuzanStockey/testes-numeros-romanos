# Projeto e documentação dos casos de teste

**Situação:** casos projetados na etapa 3, implementados e executados na etapa 4, com experimento de defeitos concluído na etapa 5. O projeto original permanece descrito abaixo; os resultados estão nos resumos das [etapas 4](resultados/etapa4/resumo.md) e [5](resultados/etapa5/resumo.md). As seções 13 e 14 registram o que foi efetivamente executado.

**Sistema sob teste:** `RomanNumerals.toRoman(int)` e `RomanNumerals.fromRoman(String)`, conforme [especificacao.md](especificacao.md).

## 1. Objetivo, escopo e técnicas

Verificar a conversão correta nas duas direções para inteiros de 1 a 3999 e suas representações romanas canônicas. Usaremos:

1. **Particionamento de equivalência (PE):** selecionar representantes de classes definidas pelas regras da representação.
2. **Análise de valores-limite (AVL):** verificar extremos válidos e vizinhos de transições de representação.
3. **Testes baseados em propriedades (PBT):** verificar relações gerais e comparar resultados com um modelo independente usando entradas geradas.

JUnit e jqwik são ferramentas de execução, não técnicas de seleção de casos. PBT também pode testar unidades; a comparação será entre exemplos fixos e propriedades geradas, e não entre “teste unitário” e “PBT”.

As técnicas podem exercitar as mesmas entradas. Um caso tem sua técnica principal identificada; não contaremos uma mesma execução duas vezes para inflar o número de testes.

Zero, valores negativos, 4000 ou maiores, `null`, texto vazio e romanos não canônicos ficam fora do contrato. Não há testes obrigatórios de rejeição ou normalização dessas entradas.

## 2. Classes de equivalência

Estas classes agrupam comportamentos relevantes da especificação; não pressupõem que um representante prove toda a classe.

**Fundamentação complementar:** [Ostrand e Balcer (1988)](https://doi.org/10.1145/62959.62964) organizam a seleção por categorias e escolhas. O texto foi acrescentado após o experimento para analisar o projeto existente: CE-S e CE-M correspondem a eixos de seleção, e os casos fixos a representantes concretos. Não foram usados TSL nem geração de todas as combinações. O [registro do terceiro artigo](pesquisa/terceiro-artigo.md) documenta essa ligação e suas limitações.

### 2.1 Eixo principal: subtração

O número de posições subtrativas é o número de dígitos 4 ou 9 nas centenas, dezenas e unidades. Os milhares não têm representação subtrativa no domínio.

| Classe | Condição | Representantes | Motivação |
|---|---|---|---|
| CE-S0 | Nenhuma posição subtrativa | 86, 1666, 3888 | Soma de símbolos e repetição |
| CE-S1 | Exatamente uma posição subtrativa | 4, 40, 400 | Subtração isolada em cada posição |
| CE-S2 | Duas ou três posições subtrativas | 49, 944, 1990, 3999 | Combinações sem interferência entre posições |

CE-S0, CE-S1 e CE-S2 são disjuntas e abrangem o domínio numérico. O mesmo agrupamento se aplica às strings canônicas correspondentes.

### 2.2 Eixo complementar: magnitude

| Classe | Intervalo | Representantes |
|---|---|---|
| CE-M1 | 1 a 9 | 1, 4, 5, 9 |
| CE-M2 | 10 a 99 | 40, 49, 50, 86, 90 |
| CE-M3 | 100 a 999 | 400, 500, 900, 944 |
| CE-M4 | 1000 a 3999 | 1000, 1666, 2008, 3999 |

As classes de magnitude também são disjuntas e completas. Elas constituem outro eixo, não devem ser somadas às classes CE-S como se todas fossem mutuamente exclusivas.

### 2.3 Categorias complementares de composição

| Categoria | Exemplos | Risco exercitado |
|---|---|---|
| Símbolo isolado | 1, 5, 10, 50, 100, 500, 1000 | Mapeamento dos sete símbolos |
| Posições zero omitidas | 1001, 2008, 1990 | Introduzir fragmento indevido ou perder posição não nula |
| Repetições aditivas | 3888 | Repetições de `M`, `C`, `X` e `I`, com `D`, `L` e `V` |
| Pares subtrativos combinados | 944, 3999 | Ordem e interpretação de múltiplos pares |

Essas categorias podem se sobrepor; servem como checklist de diversidade, não como uma terceira partição.

## 3. Convenção dos casos fixos e oráculos

Cada linha das tabelas das seções 4 e 5 representa **dois casos direcionais**:

- Sufixo `-E`: chamar `toRoman(n)` e comparar exatamente com a string literal da linha.
- Sufixo `-D`: chamar `fromRoman(r)` e comparar exatamente com o inteiro literal da linha.

Por exemplo, `CT-EQ-09-E` espera `toRoman(49) = "XLIX"`; `CT-EQ-09-D` espera `fromRoman("XLIX") = 49`. Todos têm como pré-condição as entradas válidas indicadas na linha. Não produzir a entrada de `fromRoman` chamando `toRoman`: os casos precisam de oráculos separados.

**Rastreabilidade por direção:** todos os casos `-E` verificam RF-01, RF-02, RF-03, RF-04 e RF-07. Todos os casos `-D` verificam RF-01, RF-03 e RF-06. Casos com pares subtrativos verificam adicionalmente RF-05 na direção `-E`. A tabela informa a regra específica enfatizada, sem substituir esse vínculo geral.

Resultados esperados são comparações exatas, incluindo ordem e maiúsculas. Os valores da tabela vêm dos exemplos da especificação ou foram derivados de sua tabela posicional antes da implementação. Exceção inesperada em uma entrada válida é falha do caso.

## 4. Casos por particionamento de equivalência

| ID base | Inteiro `n` | Romano `r` esperado | Classes/categoria | Regra enfatizada |
|---|---:|---|---|---|
| CT-EQ-01 | 5 | `V` | CE-S0, CE-M1, símbolo | RF-03 |
| CT-EQ-02 | 50 | `L` | CE-S0, CE-M2, símbolo | RF-03 |
| CT-EQ-03 | 500 | `D` | CE-S0, CE-M3, símbolo | RF-03 |
| CT-EQ-04 | 1000 | `M` | CE-S0, CE-M4, símbolo | RF-03, RF-04 |
| CT-EQ-05 | 1666 | `MDCLXVI` | CE-S0, CE-M4 | RF-03, RF-04; sete símbolos |
| CT-EQ-06 | 86 | `LXXXVI` | CE-S0, CE-M2 | RF-04; composição aditiva |
| CT-EQ-07 | 2008 | `MMVIII` | CE-S0, CE-M4, zeros | RF-04; omissão de centenas/dezenas |
| CT-EQ-08 | 1001 | `MI` | CE-S0, CE-M4, zeros | RF-04; posições distantes |
| CT-EQ-09 | 49 | `XLIX` | CE-S2, CE-M2 | RF-04, RF-05; duas subtrações |
| CT-EQ-10 | 944 | `CMXLIV` | CE-S2, CE-M3 | RF-04, RF-05; três subtrações |
| CT-EQ-11 | 1990 | `MCMXC` | CE-S2, CE-M4, zeros | RF-04, RF-05; unidade zero |
| CT-EQ-12 | 3888 | `MMMDCCCLXXXVIII` | CE-S0, CE-M4, repetição | RF-04, RF-07; composição aditiva extensa |

A classe CE-S1 é representada pelos centros das transições da tabela AVL. Não é necessário duplicar esses dados na tabela PE para cobrir o eixo de subtração.

## 5. Casos por análise de valores-limite

As transições selecionadas são as seis introduções de pares subtrativos e a mudança para milhares. Para cada centro `b`, verificamos `b-1`, `b` e `b+1`. Essas são fronteiras internas de representação, não limites de aceitação da entrada.

Para os limites externos, usamos o menor e o maior valor válido e seus vizinhos internos. `0` e `4000` não têm resultado esperado obrigatório porque estão fora do contrato.

| ID base | Inteiro `n` | Romano `r` esperado | Fronteira | Regra enfatizada |
|---|---:|---|---|---|
| CT-LIM-01 | 1 | `I` | Mínimo válido | RF-02, RF-03 |
| CT-LIM-02 | 2 | `II` | Vizinho do mínimo | RF-02, RF-04 |
| CT-LIM-03 | 3998 | `MMMCMXCVIII` | Vizinho do máximo | RF-02, RF-04, RF-05 |
| CT-LIM-04 | 3999 | `MMMCMXCIX` | Máximo válido | RF-02, RF-04, RF-05 |
| CT-LIM-05 | 3 | `III` | Antes de 4 | RF-04 |
| CT-LIM-06 | 4 | `IV` | Centro 4 | RF-05 |
| CT-LIM-07 | 5 | `V` | Depois de 4 | RF-03, RF-04 |
| CT-LIM-08 | 8 | `VIII` | Antes de 9 | RF-04 |
| CT-LIM-09 | 9 | `IX` | Centro 9 | RF-05 |
| CT-LIM-10 | 10 | `X` | Depois de 9 | RF-03, RF-04 |
| CT-LIM-11 | 39 | `XXXIX` | Antes de 40 | RF-04, RF-05 |
| CT-LIM-12 | 40 | `XL` | Centro 40 | RF-05 |
| CT-LIM-13 | 41 | `XLI` | Depois de 40 | RF-04, RF-05 |
| CT-LIM-14 | 89 | `LXXXIX` | Antes de 90 | RF-04, RF-05 |
| CT-LIM-15 | 90 | `XC` | Centro 90 | RF-05 |
| CT-LIM-16 | 91 | `XCI` | Depois de 90 | RF-04, RF-05 |
| CT-LIM-17 | 399 | `CCCXCIX` | Antes de 400 | RF-04, RF-05 |
| CT-LIM-18 | 400 | `CD` | Centro 400 | RF-05 |
| CT-LIM-19 | 401 | `CDI` | Depois de 400 | RF-04, RF-05 |
| CT-LIM-20 | 899 | `DCCCXCIX` | Antes de 900 | RF-04, RF-05 |
| CT-LIM-21 | 900 | `CM` | Centro 900 | RF-05 |
| CT-LIM-22 | 901 | `CMI` | Depois de 900 | RF-04, RF-05 |
| CT-LIM-23 | 999 | `CMXCIX` | Antes de 1000 | RF-04, RF-05 |
| CT-LIM-24 | 1000 | `M` | Centro 1000 | RF-03, RF-04 |
| CT-LIM-25 | 1001 | `MI` | Depois de 1000 | RF-04 |

PE e AVL possuem **três pares repetidos**: 5, 1000 e 1001. A implementação deverá armazenar cada par uma vez e associar todos os IDs e técnicas pertinentes. As tabelas apresentam 37 linhas de seleção, mas **34 pares distintos e 68 verificações direcionais distintas**. Os números de execução por grupo devem registrar se conjuntos foram executados separadamente.

## 6. Interface e independência da ordem das chamadas

### CT-API-01 - Interface Java

- **Requisitos:** RF-01 e RP-01.
- **Verificação:** compilar testes que chamem diretamente os dois métodos públicos estáticos com tipos `int -> String` e `String -> int` na classe `RomanNumerals`.
- **Resultado esperado:** compilação bem-sucedida com a interface especificada.
- **Tipo de evidência:** compatibilidade da interface; não contar como um caso funcional adicional executado.

### CT-SEQ-01 - Chamadas intercaladas

- **Requisito:** RP-02.
- **Técnica:** teste por exemplo de sequência, complementar às três técnicas principais.
- **Estado inicial:** nenhuma preparação de estado é necessária.
- **Sequência:** `toRoman(4)`, `fromRoman("MMMCMXCIX")`, `toRoman(2008)`, `fromRoman("IV")`, `toRoman(4)`, `fromRoman("MMMCMXCIX")`.
- **Resultados esperados, na ordem:** `"IV"`, `3999`, `"MMVIII"`, `4`, `"IV"`, `3999`.
- **Oráculo:** valores literais; conferir todas as seis respostas, não apenas se duas respostas repetidas são iguais.
- **Limitação:** detecta interferência nesta sequência; não prova independência para toda ordem possível.

## 7. Modelos de referência e geradores de PBT

### 7.1 Oráculos independentes

**Modelo posicional `O(n)`:** concatenar fragmentos de quatro tabelas literais de milhares, centenas, dezenas e unidades, conforme a seção 4.2 da especificação. Esse modelo ficará exclusivamente em `/tests`, sem chamar ou importar tabelas, funções auxiliares ou constantes da implementação em `/src`.

O modelo pode gerar pares `(n, O(n))` com significado conhecido. Antes de usá-lo como oráculo, validar suas tabelas contra os pares fixos das seções 4 e 5. Essa validação é da infraestrutura de teste e deverá ser registrada separadamente da avaliação do SUT.

**Oráculo de formato `C(r)`:** verificar valor não nulo, string não vazia e correspondência integral com a regex da seção 4.3 da especificação. Não usar `toRoman(fromRoman(r))` para definir se o formato é válido, porque isso introduziria dependência entre as funções testadas.

Na etapa 4, a implementação deverá usar uma estratégia distinta do modelo posicional, por exemplo emissão por valores decrescentes e leitura de símbolos/pares. Se houver coincidência de estratégia, registrá-la como risco de erros compartilhados e reforçar a conferência dos oráculos. Independência de código não garante ausência de erros comuns de entendimento.

### 7.2 G-N - Inteiros válidos

- **Domínio:** 1 a 3999 inclusive.
- **Construção:** mistura planejada de 80% de amostragem uniforme do domínio e 20% de seleção do conjunto dos 25 valores distintos da tabela AVL.
- **Finalidade:** explorar o domínio geral e aumentar a frequência de transições relevantes.
- **Pré-condições por construção:** nunca gerar valores inválidos; não usar filtro para reduzir um gerador de todos os inteiros a esse intervalo.
- **Categorias observadas:** CE-S0/CE-S1/CE-S2 e CE-M1/CE-M2/CE-M3/CE-M4; registrar também os seis centros subtrativos e os extremos quando aparecerem.
- **Redução de falhas:** manter o inteiro entre 1 e 3999. O redutor não precisa preservar a proporção da mistura ou a categoria original.

Os pesos são escolhas do nosso experimento, não números recomendados pelos artigos. A mistura não garante a presença de cada valor em toda execução; os casos fixos asseguram a verificação explícita das fronteiras escolhidas.

### 7.3 G-R - Pares com romano canônico

- **Domínio:** pares `(n, r)` com `n` em `N` e `r = O(n)`.
- **Construção:** gerar `n` por G-N e montar `r` usando o modelo de referência independente.
- **Finalidade:** testar `fromRoman` com entradas e valores esperados que não dependam de `toRoman`.
- **Redução de falhas:** reduzir `n` no domínio válido e reconstruir `r` por `O`; não reduzir caracteres isolados sem preservar canonicidade e correspondência do par.
- **Descartes esperados:** zero por pré-condição de domínio. Qualquer descarte ou entrada inválida deve ser investigado como problema da infraestrutura.

## 8. Catálogo de propriedades

Cada linha define um teste de propriedade distinto; a quantidade de entradas geradas não é a quantidade de propriedades escritas.

| ID do teste | Gerador/entrada | Verificação e resultado esperado | Requisitos/propriedades | Oráculo |
|---|---|---|---|---|
| CT-PBT-01 | G-N: `n` | `fromRoman(toRoman(n)) == n`; verificar primeiro que a saída intermediária pertence a `R` | RF-02, RF-06, RF-07; PR-01 | Inteiro original e `C` |
| CT-PBT-02 | G-R: `(n, r)` | `toRoman(fromRoman(r)) == r`; conferir primeiro que o inteiro intermediário está entre 1 e 3999 | RF-04, RF-05, RF-06; PR-02 | Romano original independente e limites do domínio |
| CT-PBT-03 | G-N: `n` | `C(toRoman(n))` deve ser verdadeiro | RF-03, RF-04, RF-05, RF-07; PR-03 | Regex integral e não vazio |
| CT-PBT-04 | G-R: `(n, r)` | Resultado de `fromRoman(r)` pertence a 1..3999 | RF-06; PR-04 | Limites literais do domínio |
| CT-PBT-05 | G-N: `n` | `toRoman(n) == O(n)` | RF-02, RF-03, RF-04, RF-05, RF-07 | Modelo posicional independente |
| CT-PBT-06 | G-R: `(n, r)` | `fromRoman(r) == n` | RF-03, RF-06 | Valor que originou o par independente |

Em CT-PBT-01, a verificação de formato antes da decodificação impede que uma saída inválida do codificador seja passada como se estivesse coberta pelo contrato do decodificador. Uma falha nessa etapa indica violação de RF-07 pelo codificador.

CT-PBT-01 e CT-PBT-02 verificam coerência entre funções. CT-PBT-03 verifica formato, e CT-PBT-04 verifica intervalo; esses critérios isolados são fracos. CT-PBT-05 e CT-PBT-06 acrescentam significado numérico independente. Não somar a quantidade de requisitos vinculados para criar uma medida de eficácia.

Uma falha de ida e volta pode envolver qualquer função, a propriedade ou o gerador. Os testes direcionais e a inspeção do contraexemplo serão necessários para diagnosticar a origem.

## 9. Configuração planejada e reprodução

- **Ferramentas previstas:** Java, Maven, JUnit e jqwik; versões exatas serão fixadas na implementação.
- **Modo principal:** geração explicitamente aleatória, evitando que a ferramenta escolha automaticamente enumeração exaustiva do pequeno domínio.
- **Tentativas:** 1000 entradas válidas por propriedade e por semente.
- **Sementes planejadas:** `42`, `2024`, `2026`, `3999` e `104729`.
- **Campanha:** seis propriedades x cinco sementes x 1000 entradas, até 30.000 avaliações primárias se todas completarem. Isso não significa 30.000 entradas distintas; repetições são possíveis. Execuções de redução são contabilizadas separadamente.
- **Ordem:** executar exemplos e propriedades em grupos separados para obter resultados de cada conjunto, além da suíte completa.
- **Falhas:** guardar a semente, entrada original quando disponível, contraexemplo reduzido, propriedade, etapa de verificação e saída observada.

Os detalhes para reproduzir cada execução (comando e configuração efetiva) serão documentados após a implementação. Se a ferramenta parar na primeira falha ou não consumir todas as tentativas, registrar o número real; não declarar 1000 entradas avaliadas em uma execução interrompida.

### Checagem exaustiva complementar

Uma execução adicional poderá percorrer os 3999 pares do modelo e verificar as duas operações separadamente, totalizando 7998 comparações direcionais. Registrar essa checagem como exaustiva e separada do grupo aleatório, especialmente na análise de defeitos. Ela não deverá ser misturada ao resultado de PBT para atribuir à amostragem uma detecção obtida por enumeração.

## 10. Matriz de rastreabilidade

| Requisito | Exemplos/limites | Propriedades e outras verificações |
|---|---|---|
| RF-01 | Todos os casos `-E` e `-D` | CT-API-01 |
| RF-02 | Todos os casos `-E`; CT-LIM-01 a 04 enfatizam extremos | CT-PBT-01, CT-PBT-05 |
| RF-03 | Símbolos isolados; CT-EQ-05; transições subtrativas | CT-PBT-03, CT-PBT-05, CT-PBT-06 |
| RF-04 | CT-EQ-07, 08, 09, 10, 11, 12; mudanças de posição na tabela AVL | CT-PBT-02, CT-PBT-03, CT-PBT-05 |
| RF-05 | Centros 4, 9, 40, 90, 400, 900; CT-EQ-09, 10, 11; CT-LIM-04 | CT-PBT-02, CT-PBT-03, CT-PBT-05 |
| RF-06 | Todos os casos `-D` | CT-PBT-01, CT-PBT-02, CT-PBT-04, CT-PBT-06 |
| RF-07 | Todas as comparações exatas `-E` | CT-PBT-01, CT-PBT-03, CT-PBT-05 |
| RP-01 | Chamadas diretas às assinaturas planejadas | CT-API-01 |
| RP-02 | CT-SEQ-01 | Revisão de dependência de estado; sequência é evidência parcial |

As propriedades PR-01 a PR-04 estão ligadas, respectivamente, a CT-PBT-01 a CT-PBT-04. A ligação teoria -> decisão de teste está registrada nos [fichamentos](pesquisa/fichamentos.md), sobretudo nas seções 3 e 4.

## 11. Avaliação planejada dos testes

Na etapa 5, introduzir temporariamente **um defeito por vez**, restaurando a implementação correta entre variantes. O objetivo será observar detecção pelos exemplos (PE + AVL), pelas propriedades e pela combinação.

| Variante planejada | Defeito | Requisito violado | Entrada que demonstra a violação |
|---|---|---|---|
| DF-01 | Codificador representa 4 aditivamente como `IIII` | RF-05, RF-07 | `toRoman(4)`, esperado `IV` |
| DF-02 | Decodificador soma todos os símbolos, ignorando subtração | RF-06 | `fromRoman("IV")`, esperado 4 |
| DF-03 | Codificador omite unidades quando há zeros nas posições internas | RF-04 | `toRoman(1001)`, esperado `MI` |
| DF-04 | Codificador trata incorretamente o máximo válido 3999 | RF-02 | `toRoman(3999)`, esperado `MMMCMXCIX` |

São definições de defeitos para uma avaliação posterior, não mudanças já feitas no código. A implementação exata de cada variante será documentada antes de sua execução. Não afirmamos antecipadamente que todas as propriedades detectarão todos os defeitos; o resultado será observado para cada semente. Estes defeitos exercitam requisitos cobertos por exemplos deliberadamente escolhidos, o que limita qualquer conclusão comparativa de superioridade.

Um defeito será classificado como detectado por um grupo se ao menos um teste desse grupo falhar devido à alteração. Problemas de compilação não contam como detecção funcional. Confirmar que a variante altera o comportamento de alguma entrada válida antes de interpretar sua sobrevivência como uma lacuna de testes.

## 12. Registro de execução e conclusão da etapa

| Campo a registrar | Conteúdo |
|---|---|
| Identificação | Versão do código, ambiente, dependências e variante de defeito |
| Seleção | IDs, grupo PE/AVL/PBT e comando executado |
| Geração | Modo efetivo, pesos, tentativas, semente, descartes e distribuição observada |
| Resultado | Aprovado/falhou, entrada, esperado, observado e diagnóstico |
| Depuração | Contraexemplo original/reduzido e passos de redução, se disponíveis |
| Tempo | Duração observada; sem limiar obrigatório de aprovação |
| Eficácia ilustrativa | Defeitos detectados por grupo e por semente, com limitações |

**Inventário planejado:** 34 pares fixos distintos (68 verificações direcionais), um caso de sequência, uma verificação de compilação da interface e seis propriedades. A validação do modelo de referência e a eventual checagem exaustiva são evidências separadas.

**Etapa 3 concluída:** técnicas, classes, entradas, resultados esperados, propriedades, geradores, oráculos, configuração e rastreabilidade documentados. O registro de execução posterior está na seção 13.

## 13. Implementação e execução da etapa 4

| Casos | Código correspondente | Estado |
|---|---|---|
| CT-EQ e CT-LIM, direções E/D | `tests/FixedCases.java` e `tests/RomanExamplesTest.java`, métodos `encodesKnownPairs` e `decodesKnownPairs` | 68 verificações aprovadas, com dados repetidos consolidados |
| CT-SEQ-01 | `tests/RomanExamplesTest.java`, `interleavedCallsDoNotChangeResults` | Aprovado |
| CT-API-01 | Compilação das chamadas públicas estáticas nos testes | Compilação aprovada |
| CT-PBT-01 a 06 | `tests/RomanPropertyChecks.java`, métodos `p01` a `p06`, chamados pelos cinco arquivos `Seed...Properties.java` | 1000 checks por propriedade/semente; 30.000 no grupo, aprovados |
| Validação dos oráculos | `tests/RomanInfrastructureTest.java` | 35 itens aprovados, separados da avaliação do SUT |
| Checagem exaustiva complementar | `tests/RomanExhaustiveTest.java` | 7998 comparações aprovadas; fora da suíte padrão |

Configuração efetiva: JDK 17, JUnit 5.13.4, jqwik 1.9.3, modo `RANDOMIZED`, casos especiais automáticos desabilitados e banco de falhas desabilitado. As sementes fixas estão nas anotações dos testes. A guarda de domínio intermediário em CT-PBT-02 foi acrescentada para não chamar `toRoman` fora do seu contrato caso `fromRoman` produza um valor inválido.

Reprodução por grupo e por método: [README.md](README.md). Horários, tempos, frequências e limitações: [resumo da etapa 4](resultados/etapa4/resumo.md). Todos os casos do SUT documentados foram implementados.

## 14. Experimento da etapa 5

O protocolo da seção 11 foi executado com quatro variantes isoladas, sem modificar os testes. Exemplos, PBT e combinação detectaram 4/4 variantes; cada variante foi detectada pelo PBT nas cinco sementes. A comparação por propriedade, contraexemplos originais/reduzidos, avaliações efetivamente consumidas e limites de interpretação estão no [resumo da etapa 5](resultados/etapa5/resumo.md).

Cada variante tem snapshot e diferença de código. A checagem exaustiva auxiliar apenas confirma o efeito dos defeitos e não compõe a métrica dos grupos. Ao final, o conversor foi restaurado byte a byte, os 14 hashes da etapa 4 foram conferidos e a suíte padrão aprovou novamente seus 134 itens. Os scripts de execução e consolidação estão em `scripts/run-experiment.py` e `scripts/summarize-experiment.py`.
