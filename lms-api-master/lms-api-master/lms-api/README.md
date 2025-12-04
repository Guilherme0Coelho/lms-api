Trabalho Prático - LMS API (Sistema de Cursos Online)
Alunos: 
Guilherme de Oliveira 
Kauan Almeida
Eduardo Cunha

Este trabalho visa permitir o desenvolvimento de uma aplicação Spring Boot completa, 
aplicando modelagem de dados, relacionamentos e operações de negócio. 
A aplicação é um Sistema de Gerenciamento de Aprendizagem (LMS) simplificado.
O sistema gerencia a oferta de cursos e o acompanhamento das matrículas. 

O modelo é composto por 8 classes de domínio , refletindo um contexto 
com múltiplas entidades relacionadas

Entidade	Função Principal
Professor	Ministra Cursos (1:N)
Aluno	Usuário que realiza Matrículas
Curso	Unidade de ensino, contém Módulos (N:1 com Professor)
Modulo	Organiza as Aulas (N:1 com Curso)
Aula	Unidade de conteúdo (N:1 com Módulo)
Matricula	Entidade de ligação entre Aluno e Curso (N:N)
MatriculaId
Chave composta para identificação da Matrícula
Certificado	Documento emitido após conclusão da Matrícula


Implementação de Relações e Chaves Obrigatórias
Um-para-Muitos (1:N)	Professor para Curso	@OneToMany em Professor e @ManyToOne em Curso.
Muitos-para-Um (N:1)	Aula para Modulo	@ManyToOne em Aula.
Muitos-para-Muitos (N:N)	Aluno e Curso	Resolvida pela entidade de ligação Matricula.
Chave Primária Simples	Professor, Aluno, Curso, etc.	@Id e @GeneratedValue(IDENTITY).
Chave Primária Composta	Matricula
Usa a classe @Embeddable MatriculaId, combinando alunoId e cursoId.

Chave Estrangeira como PK	Certificado
O Certificado utiliza o ID da Matricula através do @MapsId, evidenciando dependência total.

Foram implementadas as seguintes operações lógicas, indo além do CRUD tradicional:
Cálculo e Agregação	CursoService.calcularCargaHorariaTotal
Consulta com Múltiplos Critérios	MatriculaRepository.findMatriculasSemCertificadoByCurso
Processo Transacional	MatriculaService.realizarMatricula
Respostas Combinadas	AlunoService.gerarRelatorioGeral

Exemplos de Chamadas
Criação (CRUD)	POST	/api/alunos
Processo Transacional	POST	/api/matriculas/realizar?alunoId=1&cursoId=101
Consulta Composta	GET	/api/matriculas/pendentes?cursoId=101
Agregação	GET	/api/cursos/101/cargaHorariaTotal
Resposta Combinada	GET	/api/alunos/1/relatorio