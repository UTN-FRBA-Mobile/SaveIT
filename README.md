<a name="saveit"/>

# SaveIT (App de Gestión de Economía Hogareña)

## Indice
[Resumen](#resumen)  
[Pantalla Inicial](#pantallainicial)  
[Movimientos](#movimientos)  
[Reportes y Graficos](#reportes)  
[Ahorros](#ahorros)  
[Ingreso por Voz](#ingreso-por-voz)  
[Navegacion](#navegacion)


<a name="resumen"/>

### Resumen
Es una aplicación que permite registrar los movimientos económicos de un hogar.  
Con esta información podemos generar reportes y estadísticas que nos ayuden a tomar decisiones.

[Volver Arriba](#saveit)


<a name="pantallainicial"/>

### Pantalla Inicial
En la pantalla inicial podemos ver el período actual, el saldo del período y los ahorros totales.  
Desde esta pantalla podemos navegar a la sección de ahorros, a la carga de movimientos, a la lista de movimientos y a la lista de reportes.

![image](https://user-images.githubusercontent.com/20273903/125209122-16659200-e26d-11eb-949b-9bd65a496634.png)


[Volver Arriba](#saveit)


<a name="movimientos"/>

### Movimientos
Se pueden cargar movimientos de tipo Ingresos o Egreso, con las siguientes propiedades:  
Monto  
Moneda (ARS/USD)  
Medio de pago  
Categoría  
Fecha  
Descripción  
Ubicación

Cuando ahorramos (separamos dinero de nuestro ingreso mensual y lo guardamos), lo cargamos en el sistema como un movimiento de tipo Egreso y Categoría Ahorro.  

Cuando utilizamos dinero de nuestros ahorros (rescatamos dinero de nuestros ahorros y lo usamos como ingreso mensual), lo cargamos en el sistema como un movimiento de tipo Ingreso y Categoría Ahorro.

![image](https://user-images.githubusercontent.com/20273903/125209132-241b1780-e26d-11eb-95e8-b45457c8994b.png)


[Volver Arriba](#saveit)


<a name="reportes"/>

### Reportes y Graficos
Se pueden generar reportes de barra, de torta, de linea, sobre todos los movimientos, por semana, mes, año, etc.

![image](https://user-images.githubusercontent.com/20273903/125209186-90961680-e26d-11eb-84c6-db6ec9a5881f.png)

#### Reportes de Torta
Tenemos que elegir Tipo de Movimiento, Año y opcionalmente el mes, para graficar el reporte en forma de torta. 

![ScreenshotPieCHART](https://user-images.githubusercontent.com/11811173/125211195-25ebd780-e27b-11eb-81f4-a7ee32dbf3fd.jpg)


#### Reportes de Barra
Tenemos que elegir Tipo de Movimiento, Año y opcionalmente el mes, para graficar el reporte en forma de barra. 

![ScreenshotBarras](https://user-images.githubusercontent.com/11811173/125211201-2c7a4f00-e27b-11eb-99f1-20bfe0ce6a4e.jpg)


#### Reportes por Fecha
Tenemos que elegir Tipo de Movimiento, Categoría, Medio de Pago y Período para graficar el reporte en forma de línea en el tiempo.

![image](https://user-images.githubusercontent.com/20273903/125209283-229e1f00-e26e-11eb-96a5-20e5922ccd45.png)

![ScreenshotFechas](https://user-images.githubusercontent.com/11811173/125211219-4320a600-e27b-11eb-82c1-8e80924adf0c.jpg)


#### Reportes por Ubicación
Tenemos que elegir Año y Mes para mostrar la ubicación de los movimientos realizados en ese período.

![ScreenshotMapa](https://user-images.githubusercontent.com/11811173/125211224-49168700-e27b-11eb-90fd-6d13709f6dad.jpg)



[Volver Arriba](#saveit)


<a name="ahorros"/>

### Ahorros
En esta pantalla podemos ver el reporte desde el punto de vista de nuestros ahorros y como se van acumulando.

![image](https://user-images.githubusercontent.com/20273903/125214707-d9aa9280-e28e-11eb-9c4b-775f0a135f6d.png)


Es importante destacar que en esta vista se verá como Ingreso (Verde), todo lo que en mis movimientos marque como Egreso y Ahorro.  

Lo mismo sucede a la inversa, en esta vista, se verá como Egreso (Rojo), todo lo que en mis movimientos marque como Ingreso y Ahorro.

Es decir, en esta pantalla se ven los movimientos de ahorros desde el punto de vista de mis ahorros, no se ven desde el punto de vista del flujo económico. 

La información desde el punto de vista del flujo económico se ve reflejada en todas las demás secciones de la aplicación: la pantalla principal (Saldo), lista de movimientos, reportes, etc.


![image](https://user-images.githubusercontent.com/20273903/125209142-3b5a0500-e26d-11eb-9dff-7837952b8553.png)


[Volver Arriba](#saveit)


<a name="ingresoporvoz"/>

### Ingreso por Voz
Se pueden crear movimientos por comando de voz utilizando el asistente de Google.

[Volver Arriba](#saveit)


<a name="navegacion"/>

### Navegacion
![Screenshot_9](https://user-images.githubusercontent.com/11811173/125207274-b4535f80-e261-11eb-901f-910a0288c015.jpg)

[Volver Arriba](#saveit)


<br>

# WIP

Configurar el almacenamiento de los datos en una ruta donde el backup alcance esos datos.

Google Assistant: Permitir dar de alta un ingreso mediante google assistant, por ejemplo “OK Google, registra un gasto de $X en Y medio de pago en Z categoría

Consultar almacenamiento de perfiles y datos de usuarios -> Local?
=> SI, asegurarse que se backupee la informacion
