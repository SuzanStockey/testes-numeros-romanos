# Terceiro artigo — fundamentação do particionamento

## Identificação e busca

Thomas J. Ostrand e Marc J. Balcer. *The Category-Partition Method for Specifying and Generating Functional Tests*. Communications of the ACM, v. 31, n. 6, p. 676–686, 1988. DOI: https://doi.org/10.1145/62959.62964.

Texto completo: https://faculty.cc.gatech.edu/~harrold/6340/cs6340_fall2010/Readings/ostrandCategoryPartition88.pdf.

Expressão de busca: `equivalence partitioning software testing academic paper category partition method Ostrand Balcer 1988`.

O texto fundamenta a discussão sobre a seleção de exemplos fixos por categorias e escolhas. O QuickCheck é o texto escolhido para o E5. A ligação entre as categorias do artigo e os casos do conversor está detalhada abaixo.

## Síntese e avaliação crítica

Os autores propõem organizar testes funcionais a partir da especificação: identificar unidades, parâmetros e condições do ambiente; definir categorias; separar escolhas; restringir combinações; gerar descrições e transformá-las em casos concretos. Categorias são características da entrada; escolhas representam alternativas dentro dessas características. A ferramenta TSL apoia o processo, mas não decide automaticamente o resultado esperado dos testes.

O ponto forte é tornar explícitas as decisões que ligam requisitos a entradas e controlar combinações incompatíveis ou redundantes. A apresentação inclui o exemplo de um comando de busca e experiência de aplicação em um componente de gerenciamento de versões/configurações. Não é um ensaio controlado que prove superioridade do método. A seleção das categorias e dos resultados esperados continua dependendo do julgamento de quem projeta os testes.

## Localização no PDF

| Assunto | Páginas do PDF (1–11) |
|---|---|
| Particionamento do domínio e condições de fronteira | 1–2 |
| Categorias, escolhas, restrições e casos concretos | 2–4 |
| Exemplo de busca e controle das combinações | 4–7 |
| Experiência com TSL e erros nos próprios testes | 8–9 |
| Relação com outros métodos, incluindo equivalência | 9–10 |
| Limitação de especificação de resultados na TSL apresentada | 10 |

## Relação com o conversor já testado

O artigo fundamenta a organização manual das partições de `tests.md`, mas não torna o experimento uma aplicação completa de Category-Partition. Não foram usados TSL, seus seletores ou geração automática de todos os *test frames*.

| Elemento da seleção | Aplicação concreta | Exemplos/casos |
|---|---|---|
| Categoria de subtração | CE-S0, CE-S1, CE-S2: nenhuma, uma ou duas/três posições subtrativas | 86, 4, 49, 944; CT-EQ-06, CT-LIM-06, CT-EQ-09 e CT-EQ-10 |
| Categoria de magnitude | CE-M1 a CE-M4: 1–9, 10–99, 100–999, 1000–3999 | 5, 50, 500, 1000; CT-EQ-01 a CT-EQ-04 |
| Características de composição | Símbolos isolados, zeros internos e repetição; checklist com sobreposição, não partição única | 1001 e 3888; CT-EQ-08 e CT-EQ-12 |
| Consistência entre categorias | Escolher somente combinações realizáveis por números válidos | Magnitude 1–9 não admite duas/três posições subtrativas |
| Caso concreto e oráculo | Fixar número e romano literal; verificar cada operação separadamente | CT-EQ/CT-LIM, direções E e D |

Os 12 casos de PE são representantes, não 12 classes disjuntas. Os eixos CE-S e CE-M são partições distintas; as categorias de composição podem se sobrepor. Os 34 pares consolidados não cobrem necessariamente todas as combinações entre características, e não se alega essa cobertura.

## Comparação com os outros textos

- Ostrand e Balcer: organização da especificação para seleção de casos.
- Claessen e Hughes: propriedades executáveis e geração aleatória.
- Goldstein et al.: experiências industriais e dificuldades de uso e avaliação de PBT.

O terceiro texto fornece base para a seleção por partições e reforça a discussão da resenha. A AVL continua sendo aplicada aos extremos e às transições definidas no contrato; o artigo menciona fronteiras, mas não foi tratado como um manual completo de AVL. O requisito de três artigos foi respeitado: o referencial acadêmico agora tem três textos, além do enunciado do kata como fonte das regras.
