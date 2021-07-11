# SaveIT
SaveIt
App de Gestión de Economía Personal (SaveIT)


![Screenshot_9](https://user-images.githubusercontent.com/11811173/125207274-b4535f80-e261-11eb-901f-910a0288c015.jpg)

Es una aplicación que permite generar reportes y estadísticas en base a los egresos e ingresos que el usuario carga en la misma.

Ingresos y Egresos
Se podrán hacer ABMs de ingresos y egresos, que son entidades con los siguientes atributos:
Monto
Medio de pago
Categoría
Descripción
Fecha!

Moneda
Ubicación (provista por el GPS o por Google Assistant)

Categorías de egresos e ingresos (precargadas):
La aplicación tendrá un módulo de ABM de categorías, donde el usuario podrá personalizar las categorías que le resulten convenientes y definir su tipo (ingreso/egreso)

Medios de pago:
La aplicación tendrá un módulo de ABM de medios de pago, donde el usuario podrá personalizar los medios de pago que le resulten convenientes. Si genera un gasto con tarjeta de crédito en cuotas aparece el gasto en los meses subsiguientes.

Reportes y gráficos (tablas, evolución en el tiempo de categorías a elección)
Contará con los siguientes reportes semanales, mensuales y anuales.
El reporte será flexible, se puede elegir categorías, período de tiempo, ubicación (país), tipo de pago, etc.

ABM de Monedas (ars/usd/eur/real/etc). (precargadas)
A la hora de los reportes, se mostrarán en su moneda original.
=> Mockear el servicio de tipo de cambio

Los ahorros se pueden modelar como una categoría de tipo egreso.
Vas anotando lo que ahorraste en un determinado día. 
En la moneda que corresponda: dólares, euro, bitcoin, etc. Se puede hacer reportes y ver cómo evolucionan los movimientos de los ahorros en el tiempo (no los ahorros en sí), como se puede hacer con cualquier otra categoría.

Estadísticas de ingresos y egresos.

Configurar el almacenamiento de los datos en una ruta donde el backup alcance esos datos.

Google Assistant: Permitir dar de alta un ingreso mediante google assistant, por ejemplo “OK Google, registra un gasto de $X en Y medio de pago en Z categoría

---------

ABMs -> Categorías | Medio de Pago | Alertas | Monedas | Medios de Cobranza (?)

Consultar almacenamiento de perfiles y datos de usuarios -> Local?
=> SI, asegurarse que se backupee la informacion

---------

NO ABM DE Categorías

NO ABM DE MONEDAS

NO ABM DE MEDIOS DE PAGO


