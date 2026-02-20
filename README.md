Documentação de Requisitos

SISTEMA HUM CARE
Gestão de Leitos
Sumário

1. EQUIPE	3
2. VISÃO GERAL DO SISTEMA	3
3. PROJETO DO SISTEMA PROPOSTO	4
   3.1 INTERFACES COM SISTEMAS	4
   3.2 REQUISITOS DO SISTEMAS	4
   3.2.1 REQUISITOS FUNCIONAIS	4
   3.2.3 REQUISITOS NÃO FUNCIONAIS	5
   3.2.4 REQUISITOS DE QUALIDADE	5
   3.2.5 FLUXO DE TRABALHO PROPOSTO	5
   3.2.6 TABELA DE STATUS E CORES	6
   3.2.7 TABELA DE TIPO DE HIGIENIZAÇÃO E CORES	6
   3.3 DIAGRAMA DE ENTIDADE RELACIONAL	7
4. PROTOTIPAÇÃO	8
   4.1 VISÃO GESTOR	8
   4.2 VISÃO NIR (Núcleo Interno de Regulação)	11
   4.3 VISÃO ENFERMAGEM	12
5. FLUXO DE UTILIZAÇÃO	17
   5.1 GESTOR - Cadastrar equipe	17
   5.2 GESTOR - Central NIR	17
   5.3 GESTOR - Relatórios de atendimentos	18
   5.4 PROFISSIONAL EXECUTANTE - Iniciar a higienização	18
   5.5 PROFISSIONAL EXECUTANTE - Finalizar a higienização	18
   5.6 PROFISSIONAL EXECUTANTE - Desativar leito (manutenção)	18
   5.7 PROFISSIONAL EXECUTANTE - Ativar leito (manutenção)	18
6. DICIONÁRIO DE DADOS	19



















EQUIPE

Responsável: Elias Cesar Araujo de Carvalho
Solicitante:  
Data:
Analistas: Wagner Silva;


Redmine:


VISÃO GERAL DO SISTEMA


Desenvolver um sistema de gestão de leitos para UTI Adulto, atualizando o controle de ocupação, higienização, e manutenção de leitos, garantindo eficiência e segurança no atendimento aos pacientes.
O objetivo é proporcionar eficiência no controle dos leitos e facilitar o trabalho colaborativo entre os diferentes profissionais da equipe e gerar indicadores para melhorar a gestão.

Escopo Inicial
Gestão de leitos na UTI. (podendo ser escalável para outros setores)
Controle de Ocupação: Registro da ocupação dos leitos (paciente, dados e hora de entrada e saída).
Controle de Higienização: Acompanhamento do status de higienização após a desocupação do leito, registrando início e término do processo.
Ciclo de Manutenção: Controle de manutenção preventiva e corretiva dos leitos, registrando dados e tipos de manutenção.
Implementação e Treinamento
Fase de Implementação: Gradualmente implementar o sistema, começando pelos módulos de maior impacto.
Treinamento da Equipe: Treinar a equipe  para utilizar o novo sistema, com foco em operações de Higienização, ciclo do paciente e ciclo de manutenção.
Monitoramento e Suporte
Monitoramento Contínuo: Após a implementação, monitorar o desempenho do sistema e a integração entre os canais.



PROJETO DO SISTEMA PROPOSTO

INTERFACES COM SISTEMAS

Ator
Descrição
GESTOR
Gerencia todo o sistema
ENFERMAGEM
Tem acesso a parte executiva dos processos referentes ao leito
NIR
Visualiza em tempo real os status dos leitos


REQUISITOS DO SISTEMAS

REQUISITOS FUNCIONAIS

           Cadastro de Leitos:
Informações básicas (número do leito, localização, tipo de UTI).
Estado do leito (ocupado, livre, em higienização, em manutenção).
Gestão de Ocupação:
Admissão de pacientes (nome, identificação, dados e hora de admissão).
Liberação de leito (nome, identificação, dados e hora de alta).
Controle de Higienização:
Registro de início e término de higienização.
Alerta de higienização pendente após a saída de um paciente.
Históricos de higienização por leito.
Ciclo de Manutenção:
Registro de manutenções preventivas e corretivas.
Agendamento de manutenção.
Notificações de manutenção necessária (com base em ciclo de tempo ou eventos específicos).
Relatórios e Monitoramento:
Status em tempo real dos leitos.
Relatórios de ocupação por período.
Relatórios de higienização e manutenção por leito.
Histórico completo de cada leito.


REQUISITOS NÃO FUNCIONAIS

Usabilidade: O sistema deve ter uma interface simples e intuitiva, permitindo que usuários sem treinamento extenso possam utilizá-lo com eficiência.
Desempenho: O sistema deve ser capaz de suportar múltiplos usuários simultâneos sem comprometer a velocidade de resposta.
Segurança: O sistema deve garantir a proteção dos dados dos pacientes, atendimentos e informações sensíveis. A conformidade com a LGPD deve ser assegurada.
Disponibilidade:  O sistema deve estar disponível 24 horas por dia, 7 dias por semana, com um plano de contingência para falhas.
Escalabilidade: O sistema deve ser capaz de gerenciar um número crescente de leitos e unidades de UTI sem perda de desempenho.

REQUISITOS DE QUALIDADE

O sistema deverá ser desenvolvido para funcionar em diferentes navegadores;
Confiabilidade: O sistema deve ser confiável e garantir que os dados não sejam corrompidos durante os processos de salvamento e leitura.
Manutenção Simples: O sistema deve ser modular, permitindo que atualizações sejam feitas com facilidade.
Acessibilidade: O sistema deve ser acessível em dispositivos móveis, como tablets, para facilitar o acesso em ambientes hospitalares.


FLUXO DE TRABALHO PROPOSTO

Admissão do Paciente no LEITO: Enfermagem inicia a ocupação em um leito disponível.
Monitoramento de Ocupação: O sistema atualiza o status do leito para ocupado.
Liberação e Higienização: Enfermagem libera o leito avisando a equipe de zeladoria, o sistema muda o status para "em higienização"
Conclusão da Higienização: A equipe de higienização informa a conclusão e a Enfermagem atualiza o status do leito para "livre" após concluir a higienização.
Manutenção de Leito: O sistema monitora e agenda manutenções, atualizando o status do leito conforme necessário.
Pré Reserva do Leito: O NIR pode fazer uma pré -reserva de um Leito (em Higienização).









TABELA DE STATUS E CORES



TABELA DE TIPO DE HIGIENIZAÇÃO E CORES


MR PRETO: Destinado para leitos com alta contaminação por bactérias multirresistentes mais graves.
MR: Aplicado em leitos com contaminação controlada, mas ainda requer atenção.
SIMPLES: Leitos que passaram por uso leve e necessitam apenas de higienização de rotina.


DIAGRAMA DE ENTIDADE RELACIONAL

Com base no levantamento de requisitos, seria necessário um SCHEMA no banco de dados. Link para o pdf: DER HUM Care





PROTOTIPAÇÃO
Esta seção apresentará protótipos de telas meramente ilustrativas.
PROTÓTIPO NAVEGÁVEL HUMCAREUTI
VISÃO GESTOR

Home- Login Gestor

















Figura 1-Tela home

Área Logada- Menu Gestor

















Figura 2- Área Logada


Equipe - Cadastro Equipe

















Figura 3-Cadastro Equipe-Trocar Perfil por Área de Atuação


Novo- Cadastro profissional

















Figura 4- Cadastro profissional-Trocar Unidade por Setor




Equipe- Lista Equipe

















Figura 5-Lista Profissionais-Trocar Perfil por Área de Atuação e Unidade por Setor


Relatórios- Gerar Relatórios


















Figura 6-Relatórios - Trocar Unidade por Setor





Relatórios- Relatórios

















Figura 7- Relatórios-Trocar Unidade por Setor


VISÃO NIR (Núcleo Interno de Regulação)



Home- Login Profissional NIR


















Figura 8-Tela Home



Home- Visão Profissional NIR (se o Leito estiver no Status LIVRE o NIR pode fazer a PRÉ-RESERVA do leito)

















Figura 9-Tela visão NIR


VISÃO ENFERMAGEM


Home- Login Profissional Executante

















Figura 10-Tela Home



Home- Área Logada

















Figura 11-Tela Home



Iniciar higienização- Área Logada

















Figura 12-Início higienização






Confirma Higienização- seleciona tipo de higienização

















Figura 13- Iniciar higienização







Andamento higienização-  Higienização

















Figura 14-higienização


Finalizar higienização-

















Figura 15-Finalizar Atendimento






Nova Admissão de leito-

















Figura 16- Atender Novo Paciente


Desativar leito-

















Figura 17-Desativar leito






Leito desativado-

















Figura 18- Leito desativado

Ativar Leito-

















Figura 19- leito ativado

FLUXO DE UTILIZAÇÃO
Para explicar a utilização básica do sistema verificar essa sessão.

GESTOR - Cadastrar equipe
Para cadastro de equipe deve seguir os seguintes passos:

ACESSAR O SISTEMA; fig.1
CLICAR EM EQUIPE ; fig.2
CLICAR EM NOVO; fig.3
INFORMAR OS DADOS; fig.4
CLICAR EM SALVAR; fig.4
LISTA DE PROFISSIONAIS CADASTRADOS; fig.5

GESTOR - Central NIR
Para verificar os status dos leitos devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.1
CLICAR EM CENTRAL NIR ; fig.2
LISTAR ATENDIMENTOS; fig.9

GESTOR - Relatórios de atendimentos
Para verificar os atendimentos passados e atuais devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.1
CLICAR EM RELATÓRIOS ; fig.2
ESCOLHER O TIPO DE RELATÓRIO A SER GERADO;

PROFISSIONAL EXECUTANTE - Iniciar a higienização
Para o profissional executante iniciar uma nova higienização devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.10
ESCOLHER O LEITO A SER HIGIENIZADO (STATUS OCUPADO) ; fig.11
CLICAR EM INICIAR HIGIENIZAÇÃO; fig.12
CLASSIFICAR QUAL TIPO DE HIGIENIZAÇÃO SERÁ APLICADA ; fig.13
CLICAR EM INICIAR ATENDIMENTO; fig.13
ao final do atendimento CLICAR EM FINALIZAR ATENDIMENTO;fig.14

PROFISSIONAL EXECUTANTE - Finalizar a higienização
Para o profissional executante efetuar ALTA de um paciente devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.9
ESCOLHER O LEITO A SER HIGIENIZADO (STATUS EM HIGIENIZAÇÃO) ; fig.14
CLICAR EM FINALIZAR; fig.14
CLICAR EM SIM; fig.15
LEITO COM STATUS DISPONÍVEL PARA NOVA ADMISSÃO; fig.16

PROFISSIONAL EXECUTANTE - Desativar leito (manutenção)
Para o profissional executante desativar qualquer leito por motivos de manutenção  devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.9
ESCOLHER O LEITO A SER DESATIVADO (EM QUALQUER STATUS) ; fig.14
CLICAR EM EQUIPAMENTO EM MANUTENÇÃO; fig.16
CONFIRMAR MANUTENÇÃO; fig.17

PROFISSIONAL EXECUTANTE - Ativar leito (manutenção)
Para o profissional executante desativar qualquer leito por motivos de manutenção  devem se seguir os seguintes passos:

ACESSAR O SISTEMA; fig.9
ESCOLHER O LEITO A SER ATIVADO (STATUS DESATIVADO) ; fig.14
CLICAR EM ATIVAR LEITO; fig.18
CONFIRMAR ATIVAÇÃO; fig.19



DICIONÁRIO DE DADOS