# Especificação do sistema sob teste - Roman Numerals Helper

## 1. Objetivo e origem

Implementar duas operações em Java: converter um inteiro para sua representação romana moderna e converter uma representação romana moderna para o inteiro correspondente.

- **Desafio:** [Roman Numerals Helper](https://www.codewars.com/kata/51b66044bce5799a7f000003), Codewars, 4 kyu, autor jhoffner.
- **Versão para Java:** https://www.codewars.com/kata/51b66044bce5799a7f000003/train/java
- **Fonte consultada para o enunciado:** https://www.codewars.com/api/v1/code-challenges/51b66044bce5799a7f000003

O enunciado foi conferido na API pública, pois a página apresenta a descrição por carregamento dinâmico. Este documento é uma especificação em português, com formalizações próprias identificadas, e não uma reprodução integral do texto do desafio.

A fundamentação das escolhas de teste está em [pesquisa/fichamentos.md](pesquisa/fichamentos.md). As referências acadêmicas não substituem o enunciado como fonte das regras de conversão.

## 2. Escopo

O sistema será uma classe Java com duas funções, sem interface gráfica, entrada por terminal, banco de dados ou serviços externos. A apresentação em HTML será um artefato separado do sistema sob teste.

O domínio numérico é:

`N = {n inteiro | 1 <= n <= 3999}`.

O domínio romano `R` contém as representações canônicas desses números, em letras latinas maiúsculas, construídas pelas regras da seção 4. Há 3999 valores em cada domínio, com correspondência única entre eles.

**Política do projeto:** entradas fora de `N` ou `R` devem lançar `IllegalArgumentException`. Isso inclui zero, negativos, valores a partir de 4000, `null`, texto vazio, minúsculas, espaços, símbolos desconhecidos e formas não canônicas, como `IIII`, `IC` e `VX`. Não há normalização, remoção de espaços ou conversão de caixa.

Essa política não foi exigida pelo Codewars. O experimento das etapas 4 e 5 avaliou o snapshot identificado da campanha de defeitos; a validação de entradas e sua execução são registradas separadamente em `resultados/validacao/`.

## 3. Interface e contrato das operações

Interface proposta para o projeto:

```java
public class RomanNumerals {
    public static String toRoman(int n);
    public static int fromRoman(String romanNumeral);
}
```

O trecho apenas descreve as assinaturas; não é código compilável nem uma implementação. O nome da classe e as assinaturas são decisões do projeto. O enunciado consultado pela API confirma as duas operações, mas não fornece o código inicial Java da plataforma.

| Operação | Pré-condição | Pós-condição |
|---|---|---|
| `toRoman(n)` | Qualquer inteiro Java | Se `n` pertence a `N`, retorna o romano canônico; caso contrário, lança `IllegalArgumentException` |
| `fromRoman(r)` | Qualquer string Java, incluindo `null` | Se `r` pertence a `R`, retorna o inteiro; caso contrário, lança `IllegalArgumentException` |

Cada operação deve atender ao seu contrato individualmente. A correção de uma composição não substitui a correção das duas operações.

**Decisão de projeto:** as operações serão determinísticas e não dependerão de estado mutável compartilhado ou do ambiente. Para uma mesma entrada válida, retornarão o mesmo resultado, independentemente da ordem das chamadas. Não há requisito quantitativo de desempenho no enunciado; tempos de execução serão observações do experimento, não limites de aceitação inventados.

## 4. Regras da representação canônica

### 4.1 Símbolos e pares subtrativos

| Símbolo | Valor | Par subtrativo | Valor |
|---|---:|---|---:|
| `I` | 1 | `IV` | 4 |
| `V` | 5 | `IX` | 9 |
| `X` | 10 | `XL` | 40 |
| `L` | 50 | `XC` | 90 |
| `C` | 100 | `CD` | 400 |
| `D` | 500 | `CM` | 900 |
| `M` | 1000 | - | - |

Esses símbolos e pares constam da tabela de apoio do desafio. As regras abaixo formalizam a orientação do enunciado de representar cada posição decimal, da esquerda para a direita, omitindo posições de valor zero.

### 4.2 Decomposição por posição decimal

Seja `n = 1000*m + 100*c + 10*d + u`, com `m` entre 0 e 3 e `c`, `d` e `u` entre 0 e 9. Sua representação é a concatenação dos quatro fragmentos correspondentes:

| Dígito | Milhares | Centenas | Dezenas | Unidades |
|---:|---|---|---|---|
| 0 | vazio | vazio | vazio | vazio |
| 1 | `M` | `C` | `X` | `I` |
| 2 | `MM` | `CC` | `XX` | `II` |
| 3 | `MMM` | `CCC` | `XXX` | `III` |
| 4 | não aplicável | `CD` | `XL` | `IV` |
| 5 | não aplicável | `D` | `L` | `V` |
| 6 | não aplicável | `DC` | `LX` | `VI` |
| 7 | não aplicável | `DCC` | `LXX` | `VII` |
| 8 | não aplicável | `DCCC` | `LXXX` | `VIII` |
| 9 | não aplicável | `CM` | `XC` | `IX` |

O número zero não pertence ao domínio. Portanto, a concatenação final nunca pode ser vazia. Um zero interno é apenas uma posição omitida, como em `2008 = MMVIII`.

### 4.3 Definição independente de forma canônica

Para reconhecer o formato sem chamar `toRoman`, uma string deve ser não vazia e corresponder por inteiro ao padrão:

```text
M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})
```

O padrão sozinho admite texto vazio; a condição de não vazio é obrigatória. A correspondência deve abranger toda a string, sem aceitar prefixos, espaços ou quebras de linha adicionais.

Esse padrão é uma formalização nossa da tabela posicional. Não é uma expressão fornecida pelo Codewars nem uma exigência de usar regex na implementação. Ele pode servir posteriormente como verificação independente de formato nos testes.

### 4.4 Valor de uma entrada canônica

O valor de uma string pertencente a `R` pode ser definido pela soma dos símbolos, subtraindo um símbolo quando ele precede imediatamente outro de maior valor. Por exemplo, `XLIX` representa `-10 + 50 - 1 + 10 = 49`.

Essa definição só estabelece o valor para entradas canônicas. Aplicar a mesma conta a `IC`, por exemplo, não torna essa string parte do domínio. A especificação permite implementar a decodificação por símbolos, fragmentos ou outra estratégia equivalente.

## 5. Requisitos identificados

**Origem:** “enunciado” identifica uma regra explícita da fonte; “formalização” identifica uma consequência das regras ou uma definição precisa adotada neste documento; “projeto” identifica uma escolha de interface ou comportamento interno.

| ID | Requisito verificável | Origem |
|---|---|---|
| RF-01 | Oferecer conversão de inteiro para romano e de romano para inteiro | Enunciado |
| RF-02 | Converter corretamente todos os inteiros no intervalo de 1 a 3999 | Enunciado |
| RF-03 | Utilizar os símbolos e pares subtrativos da seção 4.1, com seus respectivos valores | Enunciado |
| RF-04 | Produzir a representação moderna por posições decimais, em ordem de milhares, centenas, dezenas e unidades, omitindo posições zero | Enunciado e formalização da seção 4.2 |
| RF-05 | Representar os casos subtrativos canonicamente: `IV`, `IX`, `XL`, `XC`, `CD` e `CM`; em particular, produzir `IV` para 4 | Enunciado e formalização da tabela de apoio |
| RF-06 | Retornar o inteiro correto para cada entrada romana canônica do domínio | Enunciado e formalização do domínio |
| RF-07 | Para entradas numéricas válidas, produzir texto não vazio, maiúsculo e canônico | Formalização das seções 4.1-4.3 |
| RP-01 | Oferecer as assinaturas Java definidas na seção 3 | Projeto |
| RP-02 | Produzir resultados determinísticos e independentes da ordem das chamadas | Projeto |
| RP-03 | Rejeitar entradas fora de `N`/`R` com `IllegalArgumentException`, sem normalizar | Política do projeto |

Os identificadores serão reutilizados em `tests.md` na etapa 3. A tabela não exige uma implementação específica nem antecipa a quantidade de casos de teste.

## 6. Exemplos de referência

### 6.1 Exemplos presentes no enunciado

Os pares abaixo aparecem nos exemplos ou na explicação do desafio. Cada par relaciona o resultado esperado de `toRoman` e de `fromRoman` dentro do contrato:

| Inteiro | Romano canônico |
|---:|---|
| 1 | `I` |
| 4 | `IV` |
| 86 | `LXXXVI` |
| 1666 | `MDCLXVI` |
| 1990 | `MCMXC` |
| 2000 | `MM` |
| 2008 | `MMVIII` |

### 6.2 Exemplos derivados das regras

Estes pares foram derivados da seção 4 para esclarecer fronteiras e combinações. Não são apresentados como exemplos publicados pelo Codewars.

| Inteiro | Romano canônico | Regra ilustrada |
|---:|---|---|
| 9 | `IX` | Subtração nas unidades |
| 40 | `XL` | Subtração nas dezenas |
| 49 | `XLIX` | Duas posições subtrativas |
| 90 | `XC` | Transição para a centena |
| 400 | `CD` | Subtração nas centenas |
| 900 | `CM` | Transição para o milhar |
| 1001 | `MI` | Omissão de zeros internos |
| 3999 | `MMMCMXCIX` | Extremo superior e três posições subtrativas |

Essa lista não é ainda o catálogo de casos de teste. O projeto sistemático dos casos, com técnicas e identificadores próprios, pertence à etapa 3.

## 7. Propriedades derivadas do contrato

Definimos `E(n) = toRoman(n)` e `D(r) = fromRoman(r)`. As seguintes relações decorrem das pós-condições e poderão fundamentar os testes de propriedades:

| ID | Relação | Domínio | Requisitos relacionados |
|---|---|---|---|
| PR-01 | `D(E(n)) = n` | Todo `n` em `N` | RF-02, RF-06 |
| PR-02 | `E(D(r)) = r` | Todo `r` em `R` | RF-04, RF-05, RF-06 |
| PR-03 | `E(n)` é não vazio e tem forma canônica | Todo `n` em `N` | RF-03, RF-04, RF-05, RF-07 |
| PR-04 | `1 <= D(r) <= 3999` | Todo `r` em `R` | RF-06 |

As propriedades são condições necessárias da correção, mas não devem ser usadas isoladamente como especificação completa. Por exemplo, duas funções que troquem consistentemente os códigos de 4 e 5 podem passar em ambas as relações de ida e volta e produzir strings canônicas. Os pares de referência continuam necessários para ancorar o significado numérico.

De forma semelhante, `E(4) = IIII` e `D(IIII) = 4` poderiam fazer PR-01 passar apesar de uma saída inválida. Esse cenário motivou a guarda de formato da campanha original. RP-03 exige que `D` rejeite `IIII`.

Para PR-02, o domínio deverá ser construído independentemente de `E` quando essa propriedade for usada como evidência adicional. Gerar todas as strings apenas chamando `E` repete a dependência da primeira composição e pode ocultar erros compartilhados.

## 8. Critérios de aceitação e rastreabilidade futura

A implementação será avaliada pela conversão correta nas duas direções, pelo respeito à representação canônica e pelas decisões de projeto RP-01 e RP-02. Na etapa 3, serão planejados exemplos, classes de equivalência, valores-limite e propriedades que rastreiem essas obrigações.

Entradas fora dos domínios válidos têm como resultado esperado `IllegalArgumentException`, por RP-03. Não há exigência de usar uma estratégia específica, alcançar um percentual de cobertura ou obter um tempo fixo de execução.

Na etapa de experimento, o número de execuções e o modo de geração serão registrados. Enumerar os 3999 inteiros verifica exaustivamente uma propriedade nesse domínio; não transforma uma propriedade incompleta em prova de toda a especificação.

**Etapa 2 concluída:** especificação registrada, regras formalizadas, contrato e requisitos identificados. Nas etapas seguintes, os casos foram documentados em [tests.md](tests.md), o sistema foi implementado em `src/RomanNumerals.java` e a execução base foi registrada em [resultados/etapa4/resumo.md](resultados/etapa4/resumo.md).
