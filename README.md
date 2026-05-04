# File System Processor

.1 Descripción del proyecto

File System Processor es una aplicación backend desarrollada en Java con Spring Boot que permite procesar archivos reales cargados desde una interfaz HTML.

El sistema permite trabajar con distintos tipos de archivo:

- `INVOICE`
- `CONTRACT`
- `REPORT`

Cada tipo de archivo tiene reglas propias de validación, transformación, almacenamiento y notificación.

El objetivo principal del proyecto es demostrar buenas prácticas de diseño, separación de responsabilidades, uso de patrones de diseño, pruebas unitarias y procesamiento extensible.

---

.2 Objetivo del ejercicio

El sistema fue diseñado para resolver el siguiente caso:

Una empresa recibe archivos de diferentes fuentes. Cada archivo puede representar una factura, un contrato o un reporte. Cada uno debe pasar por un flujo de procesamiento que incluye:

1. Validación del nombre.
2. Validación de la extensión.
3. Validación del contenido.
4. Transformación o normalización, si corresponde.
5. Almacenamiento.
6. Notificación.
7. Generación de un resultado detallado.

Además, el sistema permite procesar varios archivos como si fueran una carpeta.

---

.3 Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Maven
- JUnit 5
- Mockito
- Lombok
- HTML
- JavaScript
- MultipartFile
- Repositorio en memoria

---

.4 Estructura del proyecto

La estructura del proyecto combina una organización modular con algunas ideas de arquitectura basada en adapters y ports.

```text
com.example.filesystemprocessor

adapters/
  input/
    web/
      FileUploadController.java

  output/
    notification/
      AbstractNotifier.java
      EmailNotifier.java
      SlackNotifier.java
      SmsNotifier.java
      NotificationConfig.java
      NotificationService.java
      NotifierType.java

    persistance/
      InMemoryFileRepository.java

file/
  core/
    exception/
      DomainException.java

    i18n/
      MessageKey.java

    model/
      File.java
      Folder.java
      FileSystemElement.java
      FileType.java

    processing/
      FileProcessingStrategy.java
      AbstractFileProcessingStrategy.java
      InvoiceProcessingStrategy.java
      ContractProcessingStrategy.java
      ReportProcessingStrategy.java
      StrategyFactory.java
      FileProcessorEngine.java

    result/
      ProcessResult.java
      ProcessError.java

    service/
      FileProcessingService.java
      FileStorageService.java

    validation/
      FileValidator.java
      AbstractFileValidator.java
      FileNameValidator.java
      FileExtensionValidator.java
      FileContentValidator.java
      FileValidationContext.java
      FileValidationChainFactory.java

      content/
        ContentRule.java
        InvoiceContentRule.java
        ContractContentRule.java
        ReportContentRule.java

ports/
  output/
    FileRepository.java
    Notifier.java
5. Organización por responsabilidad
adapters

Contiene clases que conectan el sistema con el exterior.

Ejemplos:

FileUploadController: entrada HTTP.
InMemoryFileRepository: almacenamiento en memoria.
EmailNotifier, SlackNotifier, SmsNotifier: salidas de notificación.
file/core

Contiene la lógica principal del sistema.

Aquí se encuentran:

modelos
validaciones
estrategias
motor de procesamiento
servicios
resultados
excepciones
claves i18n
ports/output

Contiene interfaces o contratos que representan salidas del sistema.

Ejemplos:

FileRepository
Notifier

Estas interfaces permiten depender de abstracciones y no directamente de implementaciones concretas.

6. Flujo general del sistema

Cuando el usuario sube un archivo desde el HTML, el flujo es:

HTML
  ↓
FileUploadController
  ↓
FileProcessingService
  ↓
FileProcessorEngine
  ↓
StrategyFactory
  ↓
FileProcessingStrategy
  ↓
Validation Chain
  ↓
FileRepository
  ↓
NotificationService
  ↓
ProcessResult

Explicación:

El usuario selecciona un archivo real desde el HTML.
El controller recibe el archivo como MultipartFile.
El controller convierte el archivo a un modelo interno File.
El controller llama al FileProcessingService.
El service delega al FileProcessorEngine.
El engine usa StrategyFactory para elegir la estrategia correcta.
La estrategia valida el archivo.
Si hay errores, devuelve un ProcessResult con fallos.
Si el archivo es válido, se transforma si corresponde.
Se guarda en el repositorio.
Se notifica según el tipo de archivo.
Se devuelve el resultado al frontend.
7. Tipos de archivo soportados
INVOICE

Reglas:

Acepta .xml o .json.
Debe contener customerId.
Debe contener amount.
Se transforma a un formato interno.
Se notifica por email.

Ejemplo válido:

customerId=1 amount=100
CONTRACT

Reglas:

Acepta .pdf.
Debe contener clientName.
Debe contener signed=true.
Se guarda sin transformación.
Se notifica por email y slack.

Ejemplo válido:

clientName=Acme signed=true
REPORT

Reglas:

Acepta .csv o .xlsx.
Debe tener una cantidad mínima de filas.
Se transforma generando un resumen.
Se notifica por slack.

Ejemplo válido:

col1,col2
value1,value2
value3,value4
8. Endpoints disponibles
Subir archivo individual
POST /api/files/upload

Recibe un archivo real usando multipart/form-data.

Parámetros:

file      archivo real
fileType  INVOICE, CONTRACT o REPORT
Subir varios archivos como carpeta
POST /api/files/upload/folder

Recibe varios archivos reales usando multipart/form-data.

Parámetro:

files  lista de archivos

El backend infiere el tipo de archivo según la extensión:

.xml / .json  → INVOICE
.pdf          → CONTRACT
.csv / .xlsx  → REPORT
Listar archivos almacenados
GET /api/files

Devuelve los nombres de los archivos guardados en memoria.

Limpiar repositorio
DELETE /api/files

Limpia el repositorio en memoria.

9. Interfaz HTML

El proyecto incluye una interfaz simple en:

src/main/resources/static/index.html

Para abrirla:

http://localhost:8083

Desde la interfaz se puede:

subir un archivo real
subir varios archivos como carpeta
listar archivos guardados
limpiar el repositorio
10. Configuración del puerto

El proyecto usa el puerto:

8083

Configurado en:

src/main/resources/application.properties
server.port=8083
11. i18n

El proyecto usa claves de mensajes centralizadas en:

MessageKey.java

Ejemplo:

public static final String FILE_EXTENSION_INVALID = "file.extension.invalid";

Las traducciones están en:

src/main/resources/messages.properties

Ejemplo:

file.extension.invalid=La extensión del archivo no es válida para el tipo seleccionado.

Esto evita tener mensajes escritos directamente dentro de la lógica de negocio.

12. Patrones de diseño utilizados
Composite

Se usa para representar archivos y carpetas con una misma interfaz.

FileSystemElement
  ├── File
  └── Folder

¿Por qué se usó?

Porque el sistema debe procesar archivos individuales y también grupos de archivos como carpetas.

Chain of Responsibility

Se usa para encadenar validaciones.

FileNameValidator
  ↓
FileExtensionValidator
  ↓
FileContentValidator

¿Por qué se usó?

Porque cada validación tiene una responsabilidad específica y se pueden agregar nuevas validaciones sin modificar una clase gigante.

Strategy

Se usa para procesar cada tipo de archivo con una clase diferente.

INVOICE  → InvoiceProcessingStrategy
CONTRACT → ContractProcessingStrategy
REPORT   → ReportProcessingStrategy

¿Por qué se usó?

Porque cada tipo de archivo tiene reglas de procesamiento distintas. Esto evita condicionales grandes.

Template Method

Se usa en:

AbstractFileProcessingStrategy

Define el flujo común:

validar → transformar → guardar → notificar → devolver resultado

¿Por qué se usó?

Porque todas las estrategias comparten el mismo flujo general, pero algunas cambian pasos específicos como la transformación.

Repository

Se usa para desacoplar el almacenamiento.

Contrato:

FileRepository

Implementación:

InMemoryFileRepository

¿Por qué se usó?

Porque hoy se guarda en memoria, pero en el futuro se podría cambiar a una base de datos sin modificar demasiado el core.

Adapter

Se usa para separar detalles externos del core.

Ejemplos:

FileUploadController como adapter de entrada.
InMemoryFileRepository como adapter de salida.
EmailNotifier, SlackNotifier y SmsNotifier como adapters de salida.
13. Principios aplicados
Single Responsibility Principle

Cada clase tiene una responsabilidad específica.

Ejemplos:

FileExtensionValidator valida extensiones.
EmailNotifier simula notificación por email.
FileProcessorEngine coordina el procesamiento.
InMemoryFileRepository guarda archivos en memoria.
Open/Closed Principle

El sistema puede extenderse sin modificar demasiado lo existente.

Por ejemplo, para agregar un nuevo tipo de archivo se podría crear:

nuevo valor en FileType
nueva ContentRule
nueva ProcessingStrategy
nueva configuración de notificación
Dependency Inversion Principle

El sistema usa interfaces como:

FileRepository
Notifier

Esto evita depender directamente de implementaciones concretas.

14. Pruebas unitarias

El proyecto incluye pruebas unitarias para validar las piezas principales.

Se prueban:

modelos
resultados
excepciones
i18n
validadores
reglas de contenido
estrategias
motor de procesamiento
servicios
controller
repositorio
notificaciones