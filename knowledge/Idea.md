# Sistema de asistencia y organización de clases

## Idea general

Mi idea es hacer una aplicación de escritorio en Java para que un profesor pueda organizar sus clases y tomar asistencia. Quiero que sea un programa simple y flexible, que se pueda usar sin conexión a Internet y sin iniciar sesión. Los datos se guardarían localmente para poder cerrar y volver a abrir la aplicación sin perder la información.

La prioridad sería que el profesor pueda llegar rápido a la comisión que tiene que atender y tomar lista sin que el programa le pida pasos innecesarios.

## Qué permitiría hacer

### Materias y comisiones

El profesor podría cargar sus materias y, dentro de cada materia, crear una o más comisiones. En cada comisión se guardarían los alumnos, los días y horarios de cursada y el establecimiento donde se dan las clases. Una misma comisión podría tener más de un horario semanal.

También se podrían cargar distintos establecimientos, con su nombre y dirección, y asociarlos a los horarios o a una clase en particular.

### Alumnos y ayudantes

Se podrían registrar los alumnos y asociarlos a sus comisiones. El profesor podría consultar y actualizar sus datos cuando fuera necesario.

También se podrían registrar ayudantes de las clases. Un ayudante podría ser alguien que ya fue alumno del profesor o una persona que nunca estuvo cargada como alumno. Si ya existe como alumno, se podría vincular con ese registro para no repetir sus datos.

### Clases y asistencia

El programa permitiría consultar las clases de una fecha y abrir la lista de alumnos de la comisión correspondiente. Para cada clase se podría marcar si cada alumno estuvo presente, ausente, llegó tarde o justificó la falta. También se podrían corregir los registros si se cometió un error.

Al abrir una lista, los alumnos aparecerían inicialmente como pendientes de registrar. El profesor tendría que marcar explícitamente el estado de cada uno, para distinguir a quienes todavía no se les tomó asistencia de quienes fueron marcados como ausentes.

La asistencia correspondería a una clase concreta, así los registros anteriores se conservarían aunque después cambien los horarios o los alumnos de la comisión. Los cambios en la comisión no modificarían las listas ni las asistencias de las clases pasadas.

### Materiales y recordatorios

Para cada clase se podrían anotar los materiales que el profesor necesita llevar. Por ejemplo, se podría indicar que para la clase del 3 de determinado mes hay que llevar la computadora.

También se podrían crear recordatorios para las clases, los materiales o cualquier otro asunto. El profesor elegiría qué quiere recordar y en qué fecha y hora. La aplicación intentaría mostrar una notificación emergente de Windows cuando llegue el momento. Si no fuera posible mostrarla con la aplicación cerrada, los recordatorios vencidos aparecerían en una ventana emergente al iniciar la aplicación. Como mejora, se podría configurar la aplicación para que se inicie junto con Windows.

Los recordatorios se podrían consultar dentro de la aplicación y marcar como realizados.

## Copias de seguridad

Como los datos se guardarían únicamente en la computadora, el profesor podría crear una copia de seguridad local y restaurarla si cambia de equipo o necesita recuperar la información. La restauración pediría confirmación antes de reemplazar los datos existentes.

## Clases principales

Estas son las clases que imagino para representar la información del programa:

- **Materia:** contiene una o más comisiones.
- **Comision:** agrupa alumnos y tiene sus horarios y clases.
- **Alumno:** representa a un alumno y permite asociarlo a una o más comisiones.
- **Ayudante:** representa a un ayudante y puede vincularse con un alumno ya registrado.
- **Establecimiento:** contiene los datos del lugar donde se dictan las clases.
- **Horario:** representa un día y un horario habitual de cursada, asociado a una comisión y un establecimiento.
- **EventoAgenda:** clase abstracta con la información común de un evento que tiene fecha y hora.
- **Clase:** representa un encuentro concreto de una comisión.
- **Asistencia:** vincula a un alumno con una clase e indica su estado.
- **Material:** representa algo que se necesita para una clase.
- **Recordatorio:** representa un aviso y puede estar asociado a una clase o a un material.

Una materia tendría varias comisiones. Cada comisión tendría alumnos, horarios y clases. Cada clase tendría los registros de asistencia y podría tener materiales asociados. Un ayudante podría vincularse con un alumno, pero no sería obligatorio.

## Organización del programa

Quiero separar las clases que representan la información de la aplicación de la parte que la guarda y de la parte que permite usarla. Así, las reglas de las materias, comisiones y asistencias no dependerían directamente de cómo se muestran las pantallas.

La clase abstracta `EventoAgenda` sería la base de `Clase` y `Recordatorio`. Cada una tendría su propio comportamiento, pero ambas podrían manejarse como eventos con fecha y hora.

Usaría una interfaz `Repositorio<T>` para definir operaciones de guardado y consulta. La clase genérica `RepositorioJDBC<T>` implementaría esas operaciones para distintos tipos de datos y se ocuparía de la persistencia.

Para los estados de asistencia usaría una enumeración `EstadoAsistencia`, con los valores `SIN_REGISTRAR`, `PRESENTE`, `AUSENTE`, `TARDE` y `JUSTIFICADA`. Usaría colecciones para guardar, por ejemplo, los alumnos de una comisión, y un mapa para relacionar cada alumno con su estado de asistencia en una clase.

También habría excepciones personalizadas para manejar errores de validación y errores al guardar o consultar datos. Por ejemplo, `ValidacionException` y `PersistenciaException`.

## Persistencia

La información se guardaría localmente en una base de datos a la que accedería mediante JDBC, tal como indica la consigna. La aplicación no necesitaría un servidor ni conexión a Internet para el uso normal, y no tendría cuentas ni login.

## Relación con los requisitos del trabajo

El sistema se programaría en Java y tendría más de cinco clases, además de la clase principal. En el diseño usaría encapsulamiento, abstracción, herencia y polimorfismo. También incluiría una clase abstracta, una interfaz, una enumeración, colecciones, un mapa, una clase genérica y al menos dos excepciones personalizadas.

Como parte del trabajo prepararía un diagrama UML para mostrar las clases y sus relaciones. La persistencia se realizaría con JDBC y el código se versionaría con Git/GitHub.
