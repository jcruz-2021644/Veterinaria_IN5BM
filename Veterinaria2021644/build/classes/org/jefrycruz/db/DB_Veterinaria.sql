-- Jefry Andre Cruz Yuman
-- 2021644
-- IN5BM
-- 09/04/2025

drop database if exists DB_Veterinaria;
create database DB_Veterinaria;
use DB_Veterinaria;

-- TODAS LAS TABLAS DE LA VETERINARIA
Create table Registros(
	codigoRegistro int auto_increment,
    usuario varchar(100) UNIQUE,
    contrasena varchar(100),
    primary key PK_codigoRegistro(codigoRegistro)

);


Create table Clientes(
	codigoCliente int auto_increment,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    telefonoCliente varchar(20) not null,
    direccionCliente varchar(255) not null,
    emailCliente varchar(100),
    fechaRegistro date not null,
    primary key PK_codigoCliente (codigoCliente)
);

Create table Veterinarios(
	codigoVeterinario int auto_increment,
    nombreVeterinario varchar(50) not null,
    apellidoVeterinario varchar(50) not null,
    especialidad varchar(100) not null,
    telefonoVeterinario varchar(20) not null,
    correoVeterinario varchar(100) not null,
    fechaIngreso date not null, 
    estado enum('Activo', 'Inactivo'),
    primary key PK_codigoVeterinario (codigoVeterinario)
);

Create table Proveedores(
	codigoProveedor int auto_increment,
	nombreProveedor varchar(150) not null,
	direccionProveedor varchar(255) not null,
	telefonoProveedor varchar(20) not null,
	correoProveedor varchar(100) not null,
	primary key PK_codigoProveedor (codigoProveedor)
);

Create table Vacunas(
	codigoVacuna int auto_increment,
	nombreVacuna varchar(150) not null,
    descripcion varchar(255),
    dosis varchar(100),
    frecuenciaMeses int,
    primary key PK_codigoVacuna (codigoVacuna)
    );

Create table Empleados(
	codigoEmpleado int auto_increment,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    cargo varchar(50) not null,
    telefonoEmpleado varchar(20) not null,
    correoEmpleado varchar(100),
    primary key PK_codigoEmpleado (codigoEmpleado)
);

Create table Mascotas(
	codigoMascota int auto_increment,
    nombreMascota varchar(50) not null,
    especie varchar(30) not null,
    raza varchar(50) not null,
    sexo enum('macho', 'hembra'),
    fechaNacimiento date,
    color varchar(30),
    pesoActualKg decimal(5,2),
    codigoCliente int not null,
    primary key PK_codigoMascota (codigoMascota),
    constraint FK_Mascotas_Cliente foreign key (codigoCliente)
		references Clientes (codigoCliente)
);

Create table Consultas(
	codigoConsulta int auto_increment,
    fechaConsulta datetime not null,
    motivo varchar(255) not null,
    diagnostico varchar(255) not null,
    observaciones varchar(255),
    codigoMascota int not null,
    codigoVeterinario int not null,
    primary key PK_codigoConsulta (codigoConsulta),
    constraint FK_Consultas_Mascota foreign key (codigoMascota)
		references Mascotas (codigoMascota),
	constraint FK_Consultas_Veterinario foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

 
Create table Tratamientos(
	codigoTratamiento int auto_increment,
    descripcion varchar(255) not null,
    fechaInicio date not null not null,
    fechaFin date not null not null,
    medicamentosIndicados varchar(255),
    codigoConsulta int not null,
    primary key PK_codigoTratamiento(codigoTratamiento),
    constraint FK_Tratamientos_Consulta foreign key (codigoConsulta)
		references Consultas (codigoConsulta)
);

Create table Citas(
	codigoCita int auto_increment,
    fechaCita date not null,
    horaCita time not null,
    motivo varchar(255) not null,
    codigoMascota int not null,
    codigoVeterinario int not null,
    estado enum('Pendiente', 'Completa', 'Cancelada'),
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Mascota foreign key (codigoMascota)
		references Mascotas (codigoMascota),
	constraint FK_Citas_Veterinario foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

Create table Vacunaciones(
	codigoVacunacion int auto_increment,
    fechaAplicacion date not null,
    observaciones varchar(255),
    codigoMascota int not null,
    codigoVacuna int not null, 
    codigoVeterinario int not null,
    primary key PK_codigoVacunacion (codigoVacunacion),
    constraint FK_Vacunaciones_Mascota foreign key (codigoMascota)
		references Mascotas (codigoMascota),
	constraint FK_Vacunaciones_Vacuna foreign key (codigoVacuna)
		references Vacunas (codigoVacuna),
	constraint FK_Vacunaciones_Veterinario foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

Create table Medicamentos(
	codigoMedicamento int auto_increment,
    nombre varchar(100) not null,
    descripcion varchar(255) not null,
    stock int not null,
    precioUnitario decimal(10,2) not null,
    fechaVencimiento date,
    codigoProveedor int not null,
    primary key PK_codigoMedicamento (codigoMedicamento),
    constraint FK_Medicamentos_Proveedor foreign key (codigoProveedor)
		references Proveedores (codigoProveedor)
);

Create table Recetas(
	codigoReceta int auto_increment,
    dosis varchar(100) not null,
    frecuencia varchar(100) not null,
    duracionDosis int not null,
    indicaciones varchar(255),
    codigoConsulta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Consulta foreign key (codigoConsulta)
		references Consultas (codigoConsulta),
	constraint FK_Recetas_Medicamento foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

Create table Facturas(
	codigoFactura int auto_increment,
    fechaEmision date not null,
    total decimal(10,2) default 0.00,
    metodoPago enum('Efectivo', 'Tarjeta', 'Transferencia'),
    codigoCliente int not null,
    codigoEmpleado int not null,
    primary key PK_codigoFactura (codigoFactura),
    constraint FK_Facturas_Cliente foreign key (codigoCliente)
		references Clientes (codigoCliente),
	constraint FK_Facturas_Empleado foreign key (codigoEmpleado)
		references Empleados (codigoEmpleado)
);

Create table Compras(
	codigoCompra int auto_increment,
	fechaCompra date not null,
	total decimal(10,2) not null,
	detalle varchar(255),
	codigoProveedor int not null,
	primary key PK_codigoCompra (codigoCompra), 
	constraint FK_Compras_Proveedor foreign key (codigoProveedor)
		references Proveedores (codigoProveedor)
);

-- TODOS LOS PROCEDIMIENTOS ALMACENADOS
-- PROCEDIMIENTOS ALMACENADOS DE REGISTRO
-- Agregar REGISTRO
Delimiter //
	Create procedure sp_AgregarRegistro(
    in usuario varchar(100),
    in contrasena varchar(100))
		Begin
			Insert into Registros(usuario,contrasena)
				Values(usuario,contrasena);
        End //
Delimiter ;
call sp_AgregarRegistro("1","1");

-- Listar registros
Delimiter //
	Create procedure sp_ListarRegistros()
		Begin
			Select codigoRegistro, 
            usuario,
            contrasena 
            from Registros;
        End //
Delimiter ;
call sp_ListarRegistros();

-- Eliminar Registro
Delimiter //
	Create procedure sp_EliminarRegistro(
    in _codigoRegistro int)
		Begin
        set foreign_key_checks = 0;
			Delete from Registros
				where codigoRegistro = _codigoRegistro;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;

-- Buscar Registro
Delimiter //
	Create procedure sp_BuscarRegistro(
    in _codigoRegistro int)
		Begin
			Select codigoRegistro, usuario, contrasena from Registros 
				where codigoRegistro = _codigoRegistro;
        End //
Delimiter ;

-- Editar Registro
Delimiter //
	Create procedure sp_EditarRegistro(
     in codigoRegistro varchar(50),
    in usuario varchar(100),
    in contrasena varchar(100))
		Begin
			Update Registros R
				set R.usuario = usuario,
					R.contrasena = contrasena
						where R.codigoRegistro = codigoRegistro;
        End //
Delimiter ;

call sp_EditarRegistro("1","1","1");



-- PROCEDIMIENTOS ALMACENADOS DE CLIENTE
-- Agregar Cliente
Delimiter //
	Create procedure sp_AgregarCliente(
    in nombreCliente varchar(50),
    in apellidoCliente varchar(50), 
    in telefonoCliente varchar(20), 
    in direccionCliente varchar(255), 
    in emailCliente varchar(100), 
    in fechaRegistro date)
		Begin
			Insert into Clientes(nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro)
				Values(nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro);
        End //
Delimiter ;
call sp_AgregarCliente('Carlos', 'Ramirez', '555-1234', 'Zona# 1', 'carlos@gmail.com', '2024-01-10');
call sp_AgregarCliente('Lucia', 'Gomez', '555-2345', 'Zona# 3', 'lucia@gmail.com', '2024-01-11');
call sp_AgregarCliente('Andres', 'Perez', '555-3456', 'Zona# 5', 'andres@gmail.com', '2024-01-12');
call sp_AgregarCliente('Ana', 'Martinez', '555-4567', 'Zona# 6', 'ana@gmail.com', '2024-01-13');
call sp_AgregarCliente('Pedro', 'Suarez', '555-5678', 'Zona# 8', 'pedro@gmail.com', '2024-01-14');
call sp_AgregarCliente('Maria', 'Lopez', '555-6789', 'Zona# 3', 'maria@gmail.com', '2024-01-15');
call sp_AgregarCliente('Jose', 'Ortega', '555-7890', 'Zona# 11', 'jose@gmail.com', '2024-01-16');
call sp_AgregarCliente('Valentina', 'Diaz', '555-8901', 'Zona# 14', 'valentina@gmail.com', '2024-01-17');
call sp_AgregarCliente('Luis', 'Torres', '555-9012', 'Zona# 6', 'luis@gmail.com', '2024-01-18');
call sp_AgregarCliente('Paola', 'Morales', '555-0123', 'Zona# 10', 'paola@gmail.com', '2024-01-19');
call sp_AgregarCliente('Diego', 'Cruz', '555-4321', 'Zona# 14', 'diego@gmail.com', '2024-01-20');
call sp_AgregarCliente('Laura', 'Fernandez', '555-5432', 'Zona# 15', 'laura@gmail.com', '2024-01-21');
call sp_AgregarCliente('Manuel', 'Rojas', '555-6543', 'Zona# 17', 'manuel@gmail.com', '2024-01-22');
call sp_AgregarCliente('Isabel', 'Vega', '555-7654', 'Zona# 10', 'isabel@gmail.com', '2024-01-23');
call sp_AgregarCliente('Javier', 'Castro', '555-8765', 'Zona# 11', 'javier@gmail.com', '2024-01-24');

-- Listar Clientes
Delimiter //
	Create procedure sp_ListarClientes()
		Begin
			Select codigoCliente, nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro from Clientes;
        End //
Delimiter ;

call sp_ListarClientes();

-- Eliminar Cliente
Delimiter //
	Create procedure sp_EliminarCliente(
    in _codigoCliente int)
		Begin
        set foreign_key_checks = 0;
			Delete from Clientes
				where codigoCliente = _codigoCliente;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;

call sp_EliminarCliente(5);
call sp_EliminarCliente(6);

-- Buscar Cliente
Delimiter //
	Create procedure sp_BuscarCliente(
    in _codigoCliente int)
		Begin
			Select codigoCliente, nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro from Clientes 
				where codigoCliente = _codigoCliente;
        End //
Delimiter ;
call sp_BuscarCliente(5);

-- Editar Cliente
Delimiter //
	Create procedure sp_EditarCliente(
    in codigoCliente int,
    in nombreCliente varchar(50),
    in apellidoCliente varchar(50), 
    in telefonoCliente varchar(20), 
    in direccionCliente varchar(255), 
    in emailCliente varchar(100), 
    in fechaRegistro date)
		Begin
			Update Clientes C
				set C.nombreCliente = nombreCliente,
					C.apellidoCliente = apellidoCliente,
					C.telefonoCliente = telefonoCliente,
					C.direccionCliente = direccionCliente,
					C.emailCliente = emailCliente,
					C.fechaRegistro = fechaRegistro
						where C.codigoCliente = codigoCliente;
        End //
Delimiter ;
call sp_EditarCliente(2, 'Lucia', 'Gomez', '555-0000', 'Tierra Nueva 2', 'lucia_nuevo@gmail.com', '2024-02-01');
call sp_EditarCliente(4, 'Ana', 'Martinez', '555-9999', 'Tierra Nueva 1', 'ana_actualizado@gmail.com', '2024-02-02');

-- Buscar cliente por nombre
Delimiter //
 Create procedure sp_BuscarClientePorNombre(in nombreClienteBuscar varchar(50))
begin
   Select codigoCliente, nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro from Clientes 
    where lower(nombreCliente) = lower(nombreClienteBuscar);
end//
Delimiter ;

CALL sp_BuscarClientePorNombre('Ana'); 

-- Eliminar cliente por nombre
Delimiter //
Create procedure sp_EliminarClientePorNombre(in nombreClienteEliminar varchar(50))
begin
    set foreign_key_checks = 0;
    delete from Clientes
    where  lower(nombreCliente) = lower(nombreClienteEliminar);
    select row_count() as filasEliminadas;
    set foreign_key_checks = 1;
end //
Delimiter ;
-- CALL sp_EliminarClientePorNombre('Ana'); 

-- PROCEDIMIENTOS ALMACENADOS DE VETERINARIO
-- Agregar Veterinario
Delimiter //
	Create procedure sp_AgregarVeterinario(
    in nombreVeterinario varchar(50), 
    in apellidoVeterinario varchar(50), 
    in especialidad varchar(100), 
    in telefonoVeterinario varchar(20), 
    in correoVeterinario varchar(100),
    in fechaIngreso date, 
    in estado enum('Activo','Inactivo'))
		Begin
			Insert into Veterinarios(nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado)
				Values(nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado);
        End //
Delimiter ;
call sp_AgregarVeterinario('Roberto', 'Mendez', 'Cirugia', '600-1111', 'roberto@gmail.com', '2023-01-01', 'Activo');
call sp_AgregarVeterinario('Carmen', 'Salas', 'Dermatologia', '600-2222', 'carmen@gmail.com', '2023-01-02', 'Activo');
call sp_AgregarVeterinario('Luis', 'Garcia', 'Cardiologia', '600-3333', 'luis@gmail.com', '2023-01-03', 'Activo');
call sp_AgregarVeterinario('Elena', 'Vargas', 'Odontologia', '600-4444', 'elena@gmail.com', '2023-01-04', 'Activo');
call sp_AgregarVeterinario('Victor', 'Navarro', 'Traumatologia', '600-5555', 'victor@gmail.com', '2023-01-05', 'Activo');
call sp_AgregarVeterinario('Camila', 'Soto', 'Medicina General', '600-6666', 'camila@gmail.com', '2023-01-06', 'Activo');
call sp_AgregarVeterinario('Mario', 'Alvarez', 'Cirugia', '600-7777', 'mario@gmail.com', '2023-01-07', 'Activo');
call sp_AgregarVeterinario('Sofia', 'Reyes', 'Medicina Interna', '600-8888', 'sofia@gmail.com', '2023-01-08', 'Activo');
call sp_AgregarVeterinario('Daniel', 'Ruiz', 'Oncologia', '600-9999', 'daniel@gmail.com', '2023-01-09', 'Activo');
call sp_AgregarVeterinario('Paula', 'Castillo', 'Dermatologia', '600-0001', 'paula@gmail.com', '2023-01-10', 'Activo');
call sp_AgregarVeterinario('Esteban', 'Lopez', 'Endocrinologia', '600-0002', 'esteban@gmail.com', '2023-01-11', 'Activo');
call sp_AgregarVeterinario('Juliana', 'Perez', 'Oftalmologia', '600-0003', 'juliana@gmail.com', '2023-01-12', 'Activo');
call sp_AgregarVeterinario('Carlos', 'Mora', 'Neurologia', '600-0004', 'carlos.m@gmail.com', '2023-01-13', 'Activo');
call sp_AgregarVeterinario('Natalia', 'Ramos', 'Pediatria', '600-0005', 'natalia@gmail.com', '2023-01-14', 'Activo');
call sp_AgregarVeterinario('Gabriel', 'Herrera', 'Urgencias', '600-0006', 'gabriel@gmail.com', '2023-01-15', 'Activo');


-- Listar Veterinarios
Delimiter //
	Create procedure sp_ListarVeterinarios()
		Begin
			Select codigoVeterinario, nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado from Veterinarios;
        End //
Delimiter ;
call sp_ListarVeterinarios();

-- Eliminar Veterinario
Delimiter //
	Create procedure sp_EliminarVeterinario(
    in _codigoVeterinario int)
    Begin
		  set foreign_key_checks = 0;
			Delete from Veterinarios
				where codigoVeterinario = _codigoVeterinario;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
call sp_EliminarVeterinario(5);
call sp_EliminarVeterinario(6);

-- Buscar Veterinario
Delimiter //
	Create procedure sp_BuscarVeterinario(
    in _codigoVeterinario int)
		Begin
			Select codigoVeterinario, nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado from Veterinarios
				where codigoVeterinario = _codigoVeterinario;
        End //
Delimiter ;
call sp_BuscarVeterinario(2);

-- Editar Veterinario
Delimiter //
	Create procedure sp_EditarVeterinario(
    in codigoVeterinario int,
    in nombreVeterinario varchar(50), 
    in apellidoVeterinario varchar(50), 
    in especialidad varchar(100), 
    in telefonoVeterinario varchar(20), 
    in correoVeterinario varchar(100),
    in fechaIngreso date, 
    in estado enum('Activo','Inactivo'))
		Begin
			Update Veterinarios V
				set V.nombreVeterinario = nombreVeterinario,
					V.apellidoVeterinario = apellidoVeterinario,
					V.especialidad = especialidad,
					V.telefonoVeterinario = telefonoVeterinario,
					V.correoVeterinario = correoVeterinario,
					V.fechaIngreso = fechaIngreso,
					V.estado = estado
					where V.codigoVeterinario = codigoVeterinario;
        End //
Delimiter ;
call sp_EditarVeterinario(2, 'Carmen', 'Salas', 'Oncologia', '600-2229', 'carmen_actualizada@gmail.com', '2023-02-01', 'Activo');
call sp_EditarVeterinario(4, 'Elena', 'Vargas', 'Radiologia', '600-4499', 'elena_actualizada@gmail.com', '2023-02-02', 'Activo');

-- PROCEDIMIENTOS ALMACENADOS DE PROVEEDORES
-- Agregar Proveedor
Delimiter //
	Create procedure sp_AgregarProveedor(
    in nombreProveedor varchar(50), 
    in direccionProveedor varchar(255), 
    in telefonoProveedor varchar(20), 
    in correoProveedor varchar(100))
		Begin
			Insert into Proveedores(nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor)
				Values(nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor);
        End //
Delimiter ;
call sp_AgregarProveedor('Distribuidora AnimalCare', 'Decima Avenida zona #1', '555123456', 'contacto@animalcare.com');
call sp_AgregarProveedor('Veterinaria Total', 'Calle 45 Landivar', '555456789', 'ventas@vetotal.com');
call sp_AgregarProveedor('BioMascotas S.A.', 'Zona #3 por el basurero', '555789012', 'info@biomascotas.com');
call sp_AgregarProveedor('PetSalud Proveedor', 'Ruta Atlatico km 32', '555321654', 'pedidos@petsalud.com');
call sp_AgregarProveedor('Farmavet Express', 'Zona 11 Bodegas', '555963852', 'farmavet@correo.com');
call sp_AgregarProveedor('Mascotines S.R.L.', 'Zona 5 bodelas el lomo', '555147258', 'contacto@mascotines.com');
call sp_AgregarProveedor('Distribuciones PetZone', 'Zona 3 Casa General', '555753159', 'ventas@petzone.com');
call sp_AgregarProveedor('Animal Depot', 'Por el parque de la industria', '555852456', 'info@animaldepot.com');
call sp_AgregarProveedor('SuperMascotas', 'Calle 2 Terminal Zona 9', '555159357', 'ventas@supermascotas.com');
call sp_AgregarProveedor('VeteriDistribuciones', 'Terminal Bodegas', '555258369', 'contacto@veteridist.com');
call sp_AgregarProveedor('Caninos y Felinos Pro', 'Zona 9 Por la torre del reformador', '555369852', 'soporte@cyfpro.com');
call sp_AgregarProveedor('AgroVet Internacional', 'Zona 10 majadas', '555951753', 'ventas@agrovet.com');
call sp_AgregarProveedor('Zoologica Mayorista', 'Tierra nueva 1 zona 11', '555357159', 'info@zoologica.com');
call sp_AgregarProveedor('Mascotando Distribuciones', 'Tierra nueva 2 Zona 11', '555753456', 'pedidos@mascotando.com');
call sp_AgregarProveedor('VetLife Solutions', 'Bodegas las tunas Zona 11', '555456321', 'info@vetlife.com');


-- Listar Proveedores
Delimiter //
	Create procedure sp_ListarProveedores()
		Begin
			Select codigoProveedor, nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor from Proveedores;
        End //
Delimiter ;
 call sp_ListarProveedores();

-- Eliminar Proveedor
Delimiter //
	Create procedure sp_EliminarProveedor(
    in _codigoProveedor int)
		Begin
			  set foreign_key_checks = 0;
			Delete from Proveedores
				where codigoProveedor = _codigoProveedor;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
call sp_EliminarProveedor(5);
call sp_EliminarProveedor(6);


-- Buscar Proveedor
Delimiter //
	Create procedure sp_BuscarProveedor(
    in _codigoProveedor int)
		Begin
			Select codigoProveedor, nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor from Proveedores
				where codigoProveedor = _codigoProveedor;
        End //
Delimiter ;
call sp_BuscarProveedor(3);

-- Editar Proveedor
Delimiter //
	Create procedure sp_EditarProveedor(
    in codigoProveedor int,
    in nombreProveedor varchar(50), 
    in direccionProveedor varchar(255), 
    in telefonoProveedor varchar(20), 
    in correoProveedor varchar(100))
		Begin
			Update Proveedores P 
				set P.nombreProveedor = nombreProveedor,
					P.direccionProveedor = direccionProveedor,
					P.telefonoProveedor = telefonoProveedor,
					P.correoProveedor = correoProveedor
					where P.codigoProveedor = codigoProveedor;
        End //
Delimiter ;
call sp_EditarProveedor(2, 'Distribuciones Lopez Actualizado', 'Nueva Direccion 202', '700-2229', 'actualizado@lopez.com');
call sp_EditarProveedor(4, 'Mundo Animal Plus', 'Nueva Direccion 204', '700-4449', 'nuevo@mundoanimal.com');


--  PROCEDIMIENTOS ALMACENADOS DE VACUNAS
-- Agregar Vacunas
Delimiter //
	Create procedure sp_AgregarVacuna(
	in nombreVacuna varchar(150), 
    in descripcion varchar(255), 
    in dosis varchar(100), 
    in frecuenciaMeses int)
		Begin
			Insert into Vacunas(nombreVacuna, descripcion, dosis, frecuenciaMeses)
				Values(nombreVacuna, descripcion, dosis, frecuenciaMeses);
        End //
Delimiter ;
call sp_AgregarVacuna('Rabia', 'Previene la rabia en mascotas', '1 ml', 12);
call sp_AgregarVacuna('Parvovirus', 'Prevencion del parvovirus', '1 ml', 6);
call sp_AgregarVacuna('Anti-Rabiosis', 'Control temprano para la rabia', '0.5 ml', 12);
call sp_AgregarVacuna('Hepatitis', 'Prevencion de hepatitis infecciosa', '1 ml', 12);
call sp_AgregarVacuna('Leptospirosis', 'Protege contra leptospira', '1 ml', 6);
call sp_AgregarVacuna('Moquillo', 'Previene enfermedades respiratorias', '0.5 ml', 12);
call sp_AgregarVacuna('Tos de las perreras', 'Proteccion contra bordetella', '1 ml', 12);
call sp_AgregarVacuna('Coronavirus canino', 'Previene enfermedades intestinales', '1 ml', 12);
call sp_AgregarVacuna('Panleucopenia felina', 'Virus felino comun', '0.5 ml', 12);
call sp_AgregarVacuna('Leucemia felina', 'Previene la leucemia viral felina', '1 ml', 12);
call sp_AgregarVacuna('Rabia felina', 'Prevencion contra rabia en gatos', '1 ml', 12);
call sp_AgregarVacuna('Clamidiosis', 'Control de infeccion ocular', '0.5 ml', 12);
call sp_AgregarVacuna('Brucelosis', 'Protege contra brucella', '1 ml', 12);
call sp_AgregarVacuna('Tifoidea felina', 'Prevencion de tifoidea', '1 ml', 12);
call sp_AgregarVacuna('Calicivirus felino', 'Proteccion respiratoria felina', '1 ml', 12);


-- Listar Vacunas
Delimiter //
	Create procedure sp_ListarVacunas()
		Begin
			Select codigoVacuna, nombreVacuna, descripcion, dosis, frecuenciaMeses from Vacunas;
        End //
Delimiter ;
call sp_ListarVacunas();

-- Eliminar Vacunas
Delimiter //
	Create procedure sp_EliminarVacuna(
    in _codigoVacuna int)
		Begin
        set foreign_key_checks = 0;
			Delete from Vacunas
				where codigoVacuna = _codigoVacuna;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
call sp_EliminarVacuna(5);
call sp_EliminarVacuna(6);


-- Buscar Vacunas
Delimiter //
	Create procedure sp_BuscarVacuna(
    in _codigoVacuna int)
		Begin
			Select codigoVacuna, nombreVacuna, descripcion, dosis, frecuenciaMeses from Vacunas
				where codigoVacuna = _codigoVacuna;
        End //
Delimiter ;
call sp_BuscarVacuna(3);

-- Editar Vacunas
Delimiter //
	Create procedure sp_EditarVacuna(
    in codigoVacuna int,
    in nombreVacuna varchar(150), 
    in descripcion varchar(255), 
    in dosis varchar(100), 
    in frecuenciaMeses int)
		Begin
			Update Vacunas VA
				set VA.nombreVacuna = nombreVacuna,
					VA.descripcion = descripcion,
					VA.dosis = dosis,
					VA.frecuenciaMeses = frecuenciaMeses
					where VA.codigoVacuna = codigoVacuna;
        End //
Delimiter ;
call sp_EditarVacuna(2, 'Parvovirus Plus', 'Vacuna reforzada para parvovirus', '1.5 ml', 9);
call sp_EditarVacuna(4, 'Hepatitis Avanzada', 'Nueva version contra hepatitis', '1.2 ml', 10);

-- PROCEDIMIENTOS ALMACENADOS DE EMPLEADO
-- Agregar Empleado
Delimiter //
	Create procedure sp_AgregarEmpleado(
    in nombreEmpleado varchar(50), 
    in apellidoEmpleado varchar(50), 
    in cargo varchar(50), 
    in telefonoEmpleado varchar(20), 
    in correoEmpleado varchar(100))
		Begin
			Insert into Empleados(nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado)
				Values(nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado);
        End //
Delimiter ;
call sp_AgregarEmpleado('Carla', 'Silva', 'Recepcionista', '800-1111', 'carla@vet.com');
call sp_AgregarEmpleado('Martin', 'Cano', 'Asistente', '800-2222', 'martin@vet.com');
call sp_AgregarEmpleado('Eduardo', 'hor', 'Secretario', '800-3333', 'hor@vet.com');
call sp_AgregarEmpleado('Fernando', 'Perez', 'Administrador', '800-4444', 'fernando@vet.com');
call sp_AgregarEmpleado('Luisa', 'Rodriguez', 'Veterinario', '800-5555', 'luisa@vet.com');
call sp_AgregarEmpleado('Ivan', 'Garcia', 'Encargado de limpieza', '800-6666', 'ivan@vet.com');
call sp_AgregarEmpleado('Patricia', 'Lopez', 'Contador', '800-7777', 'patricia@vet.com');
call sp_AgregarEmpleado('Alvaro', 'Mendoza', 'Soporte tecnico', '800-8888', 'alvaro@vet.com');
call sp_AgregarEmpleado('Sara', 'Castillo', 'Atencion al cliente', '800-9999', 'sara@vet.com');
call sp_AgregarEmpleado('Esteban', 'Diaz', 'Auxiliar', '800-0001', 'esteban@vet.com');
call sp_AgregarEmpleado('Isabel', 'Cruz', 'Coordinador', '800-0002', 'isabel@vet.com');
call sp_AgregarEmpleado('Julio', 'Morales', 'Supervisor', '800-0003', 'julio@vet.com');
call sp_AgregarEmpleado('Andrea', 'Gomez', 'Asistente medico', '800-0004', 'andrea@vet.com');
call sp_AgregarEmpleado('Daniel', 'Vera', 'Encargado de stock', '800-0005', 'daniel@vet.com');
call sp_AgregarEmpleado('Gabriela', 'Farias', 'Tecnico de laboratorio', '800-0006', 'gabriela@vet.com');


-- Listar Empleados
Delimiter //
	Create procedure sp_ListarEmpleados()
		Begin
			Select codigoEmpleado, nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado from Empleados;
        End //
Delimiter ;
 call sp_ListarEmpleados();

-- Eliminar Empleado
Delimiter //
	Create procedure sp_EliminarEmpleado(
    in _codigoEmpleado int)
		Begin
        set foreign_key_checks = 0;
			Delete from Empleados
				where codigoEmpleado = _codigoEmpleado;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
 call sp_EliminarEmpleado(5);
 call sp_EliminarEmpleado(6);

-- Buscar Empleado
Delimiter //
	Create procedure sp_BuscarEmpleado(
    in _codigoEmpleado int)
		Begin
			Select codigoEmpleado, nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado from Empleados
				where codigoEmpleado = _codigoEmpleado;
        End //
Delimiter ;
 call sp_BuscarEmpleado(3);

-- Editar Empleado
Delimiter //
	Create procedure sp_EditarEmpleado(
    in codigoEmpleado int,
    in nombreEmpleado varchar(50), 
    in apellidoEmpleado varchar(50), 
    in cargo varchar(50), 
    in telefonoEmpleado varchar(20), 
    in correoEmpleado varchar(100))
		Begin
			Update Empleados E
				set E.nombreEmpleado = nombreEmpleado,
					E.apellidoEmpleado = apellidoEmpleado,
					E.cargo = cargo,
					E.telefonoEmpleado = telefonoEmpleado,
					E.correoEmpleado = correoEmpleado
						where E.codigoEmpleado = codigoEmpleado;
        End //
Delimiter ;
call sp_EditarEmpleado(2, 'Martin', 'Cano', 'Supervisor de turno', '800-2229', 'martin_supervisor@vet.com');
call sp_EditarEmpleado(4, 'Fernando', 'Perez', 'Administrador General', '800-4449', 'fernando_admin@vet.com');


-- PROCEDIMIENTOS ALMACENADOS DE MASCOTA
-- Agregar Mascota
Delimiter //
	Create procedure sp_AgregarMascota(
	in nombreMascota varchar(50), 
    in especie varchar(30), 
    in raza varchar(50), 
    in sexo enum('macho','hembra'), 
    in fechaNacimiento date, 
    in color varchar(30), 
	in pesoActualKg decimal(5,2), 
    in codigoCliente int)
		Begin
			Insert into Mascotas(nombreMascota, especie, raza, sexo, fechaNacimiento, color, pesoActualKg, codigoCliente)
				Values(nombreMascota, especie, raza, sexo, fechaNacimiento, color, pesoActualKg, codigoCliente);
        End //
Delimiter ;
call sp_AgregarMascota('Max', 'Perro', 'Bulldog', 'macho', '2020-05-15', 'marron', 10.5, 1);
call sp_AgregarMascota('Bella', 'Gato', 'Siames', 'hembra', '2021-07-10', 'blanco', 3.2, 2);
call sp_AgregarMascota('Rocky', 'Perro', 'Labrador', 'macho', '2019-02-20', 'negro', 25.0, 3);
call sp_AgregarMascota('Luna', 'Gato', 'Persa', 'hembra', '2022-03-05', 'gris', 4.8, 4);
call sp_AgregarMascota('Rex', 'Perro', 'Rottweiler', 'macho', '2021-10-11', 'negro', 35.0, 1);
call sp_AgregarMascota('Misty', 'Gato', 'Maine Coon', 'hembra', '2020-08-19', 'marrón', 6.0, 2);
call sp_AgregarMascota('Coco', 'Perro', 'Beagle', 'macho', '2018-04-30', 'blanco', 12.3, 7);
call sp_AgregarMascota('Simba', 'Gato', 'Bengali', 'macho', '2017-09-12', 'naranja', 8.2, 8);
call sp_AgregarMascota('Shadow', 'Perro', 'Doberman', 'hembra', '2021-01-22', 'negro', 28.5, 9);
call sp_AgregarMascota('Bella', 'Gato', 'Maine Coon', 'hembra', '2020-11-25', 'gris', 7.0, 10);
call sp_AgregarMascota('Toby', 'Perro', 'Chihuahua', 'macho', '2021-02-10', 'blanco', 2.5, 11);
call sp_AgregarMascota('Nala', 'Gato', 'Esfinge', 'hembra', '2019-12-16', 'sin pelo', 4.5, 12);
call sp_AgregarMascota('Bruno', 'Perro', 'Pitbull', 'macho', '2020-06-02', 'gris', 20.0, 13);
call sp_AgregarMascota('Zara', 'Gato', 'Siames', 'hembra', '2022-04-19', 'blanco', 3.5, 14);
call sp_AgregarMascota('Spike', 'Perro', 'Poodle', 'macho', '2021-05-14', 'blanco', 7.5, 15);

-- Listar Mascotas
Delimiter //
	Create procedure sp_ListarMascotas()
		Begin
			Select codigoMascota, nombreMascota, especie, raza, sexo, fechaNacimiento, color, pesoActualKg, codigoCliente from Mascotas;
        End //
Delimiter ;
 call sp_ListarMascotas();

-- Eliminar Mascotas
Delimiter //
	Create procedure sp_EliminarMascota(
    in _codigoMascota int)
		Begin
        set foreign_key_checks = 0;
			Delete from Mascotas
				where codigoMascota = _codigoMascota;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
 call sp_EliminarMascota(5);
 call sp_EliminarMascota(6);

-- Buscar Mascota
Delimiter //
	Create procedure sp_BuscarMascota(
    in _codigoMascota int)
		Begin
			Select codigoMascota, nombreMascota, especie, raza, sexo, fechaNacimiento, color, pesoActualKg, codigoCliente from Mascotas
				where codigoMascota = _codigoMascota;
        End //
Delimiter ;
 call sp_BuscarMascota(3);

-- Editar Mascota
Delimiter //
	Create procedure sp_EditarMascota(
    in codigoMascota int,
    in nombreMascota varchar(50), 
    in especie varchar(30), 
    in raza varchar(50), 
    in sexo enum('macho','hembra'), 
    in fechaNacimiento date, 
    in color varchar(30), 
	in pesoActualKg decimal(5,2), 
    in codigoCliente int)
		Begin
			Update Mascotas M
				set M.nombreMascota = nombreMascota,
                M.especie = especie,
                M.raza = raza,
                M.sexo = sexo,
                M.fechaNacimiento = fechaNacimiento,
                M.color = color,
                M.pesoActualKg = pesoActualKg,
                M.codigoCliente = codigoCliente
					where M.codigoMascota = codigoMascota;
        End //
Delimiter ;
call sp_EditarMascota(2, 'Bella', 'Gato', 'Siames', 'hembra', '2021-07-10', 'blanco', 3.2, 2);
call sp_EditarMascota(7, 'Coco', 'Perro', 'Beagle', 'macho', '2018-04-30', 'blanco', 12.3, 7);

-- PROCEDIMIENTOS ALMACENADOS DE CONSULTA
-- Agregar Consulta
Delimiter //
	Create procedure sp_AgregarConsulta(
    in fechaConsulta datetime, 
    in motivo varchar(255), 
    in diagnostico varchar(255), 
    in observaciones varchar(255), 
    in codigoMascota int, 
    in codigoVeterinario int)
		Begin
			Insert into Consultas(fechaConsulta, motivo, diagnostico, observaciones, codigoMascota, codigoVeterinario)
				Values(fechaConsulta, motivo, diagnostico, observaciones, codigoMascota, codigoVeterinario);
        End //
Delimiter ;
call sp_AgregarConsulta('2025-04-01 10:00:00', 'Chequeo general', 'Todo en orden', 'No se observan problemas', 1, 1);
call sp_AgregarConsulta('2025-04-02 11:15:00', 'Vómitos frecuentes', 'Gastritis leve', 'Se recomienda dieta blanda', 2, 2);
call sp_AgregarConsulta('2025-04-03 09:30:00', 'Lesión en pata', 'Fractura leve', 'Reposo y analgésicos', 3, 3);
call sp_AgregarConsulta('2025-04-04 12:00:00', 'Catarro', 'Infección respiratoria', 'Antibióticos recetados', 4, 4);
call sp_AgregarConsulta('2025-04-05 14:00:00', 'Examen dental', 'Dientes sanos', 'Se recomienda limpieza', 1, 1);
call sp_AgregarConsulta('2025-04-06 10:30:00', 'Chequeo general', 'Todo en orden', 'No se observan problemas', 2, 2);
call sp_AgregarConsulta('2025-04-07 08:45:00', 'Vacunación', 'Vacuna de la rabia', 'Se aplicó correctamente', 7, 7);
call sp_AgregarConsulta('2025-04-08 11:00:00', 'Control de peso', 'Obesidad leve', 'Recomendación de dieta', 8, 8);
call sp_AgregarConsulta('2025-04-09 16:00:00', 'Revisión de piel', 'Alergia leve', 'Tratamiento con pomada', 9, 9);
call sp_AgregarConsulta('2025-04-10 13:00:00', 'Chequeo general', 'Todo en orden', 'No se observan problemas', 10, 10);
call sp_AgregarConsulta('2025-04-11 15:30:00', 'Catarro', 'Infección respiratoria', 'Antibióticos recetados', 11, 11);
call sp_AgregarConsulta('2025-04-12 09:30:00', 'Lesión en pata', 'Fractura leve', 'Reposo y analgésicos', 12, 12);
call sp_AgregarConsulta('2025-04-13 10:15:00', 'Vómitos frecuentes', 'Gastritis leve', 'Se recomienda dieta blanda', 13, 13);
call sp_AgregarConsulta('2025-04-14 12:45:00', 'Examen dental', 'Dientes sanos', 'Se recomienda limpieza', 14, 14);
call sp_AgregarConsulta('2025-04-15 14:30:00', 'Chequeo general', 'Todo en orden', 'No se observan problemas', 15, 15);

-- Listar Consultas
Delimiter //
	Create procedure sp_ListarConsultas()
		Begin
			Select codigoConsulta, fechaConsulta, motivo, diagnostico, observaciones, codigoMascota, codigoVeterinario from Consultas;
        End //
Delimiter ;
call sp_ListarConsultas();

-- Eliminar Consulta
Delimiter //
	Create procedure sp_EliminarConsulta(
    in _codigoConsulta int)
		Begin
        set foreign_key_checks = 0;
			Delete from Consultas
				where codigoConsulta = _codigoConsulta;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
        End //
Delimiter ;
call sp_EliminarConsulta(5);
call sp_EliminarConsulta(6);

-- Buscar Consulta
Delimiter //
	Create procedure sp_BuscarConsulta(
    in _codigoConsulta int)
		Begin
			Select codigoConsulta, fechaConsulta, motivo, diagnostico, observaciones, codigoMascota, codigoVeterinario from Consultas
				where codigoConsulta = _codigoConsulta;
        End //
Delimiter ;
call sp_BuscarConsulta(3);

-- Editar Consulta
Delimiter //
	Create procedure sp_EditarConsulta(
    in codigoConsulta int,
	in fechaConsulta datetime, 
    in motivo varchar(255), 
    in diagnostico varchar(255), 
    in observaciones varchar(255), 
    in codigoMascota int, 
    in codigoVeterinario int)
		Begin
			Update Consultas C
				set C.fechaConsulta = fechaConsulta,
					C.motivo = motivo,
					C.diagnostico = diagnostico, 
					C.observaciones = observaciones,
					C.codigoMascota = codigoMascota,
					C.codigoVeterinario = codigoVeterinario
						where C.codigoConsulta = codigoConsulta;
        End //
Delimiter ;
call sp_EditarConsulta(2, '2025-04-02 11:15:00', 'Vómitos frecuentes', 'Gastritis leve', 'Se recomienda dieta blanda', 2, 2);
call sp_EditarConsulta(7, '2025-04-07 08:45:00', 'Vacunación', 'Vacuna de la rabia', 'Se aplicó correctamente', 7, 7);

-- PROCEDIMIENTOS ALMACENADOS DE TRATAMIENTO
-- Agregar Tratamiento
Delimiter //
	Create procedure sp_AgregarTratamiento(
	in descripcion varchar(255), 
    in fechaInicio date, 
    in fechaFin date, 
    in medicamentosIndicados varchar(255),  
    in codigoConsulta int)
		Begin
			Insert into Tratamientos(descripcion, fechaInicio, fechaFin, medicamentosIndicados, codigoConsulta)
				Values(descripcion, fechaInicio, fechaFin, medicamentosIndicados, codigoConsulta);
        End //
Delimiter ;
call sp_AgregarTratamiento('Tratamiento para gastritis', '2025-04-01', '2025-04-10', 'Omeprazol 20mg', 1);
call sp_AgregarTratamiento('Fractura en pata', '2025-04-02', '2025-04-12', 'Analgésicos y yeso', 2);
call sp_AgregarTratamiento('Alergia cutánea', '2025-04-03', '2025-04-13', 'Pomada corticoide', 3);
call sp_AgregarTratamiento('Infección respiratoria', '2025-04-04', '2025-04-14', 'Amoxicilina 500mg', 4);
call sp_AgregarTratamiento('Chequeo dental', '2025-04-05', '2025-04-15', 'Limpieza dental', 1);
call sp_AgregarTratamiento('Obesidad leve', '2025-04-06', '2025-04-16', 'Dieta controlada', 2);
call sp_AgregarTratamiento('Vacunación', '2025-04-07', '2025-04-17', 'Vacuna de la rabia', 7);
call sp_AgregarTratamiento('Lesión en pata', '2025-04-08', '2025-04-18', 'Reposo y curaciones', 8);
call sp_AgregarTratamiento('Infección estomacal', '2025-04-09', '2025-04-19', 'Antibióticos y dieta blanda', 9);
call sp_AgregarTratamiento('Chequeo general', '2025-04-10', '2025-04-20', 'Recomendación de dieta', 10);
call sp_AgregarTratamiento('Desparasitación', '2025-04-11', '2025-04-21', 'Pyrantel', 11);
call sp_AgregarTratamiento('Chequeo de piel', '2025-04-12', '2025-04-22', 'Tratamiento para hongos', 12);
call sp_AgregarTratamiento('Infección respiratoria', '2025-04-13', '2025-04-23', 'Antibióticos y nebulizaciones', 13);
call sp_AgregarTratamiento('Fractura de pata', '2025-04-14', '2025-04-24', 'Yeso y antibióticos', 14);
call sp_AgregarTratamiento('Chequeo general', '2025-04-15', '2025-04-25', 'Todo en orden', 15);


-- Listar Tratamientos
Delimiter //
	Create procedure sp_ListarTratamientos()
		Begin
			Select codigoTratamiento, descripcion, fechaInicio, fechaFin, medicamentosIndicados, codigoConsulta from Tratamientos;
        End //
Delimiter ;
call sp_ListarTratamientos();

-- Eliminar Tratamiento
Delimiter //
	Create procedure sp_EliminarTratamiento(
    in _codigoTratamiento int)
	Begin
        set foreign_key_checks = 0;
			Delete from Tratamientos
				where codigoTratamiento = _codigoTratamiento;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
 call sp_EliminarTratamiento(5);
 call sp_EliminarTratamiento(6);

-- Buscar Tratamiento
Delimiter //
	Create procedure sp_BuscarTratamiento(
    in _codigoTratamiento int)
		Begin
			Select codigoTratamiento, descripcion, fechaInicio, fechaFin, medicamentosIndicados, codigoConsulta from Tratamientos
				where codigoTratamiento = _codigoTratamiento;
        End //
Delimiter ;
 call sp_BuscarTratamiento(3);

-- Editar Tratamiento
Delimiter //
	Create procedure sp_EditarTratamiento(
    in codigoTratamiento int,
    in descripcion varchar(255), 
    in fechaInicio date, 
    in fechaFin date, 
    in medicamentosIndicados varchar(255),  
    in codigoConsulta int)
		Begin
			Update Tratamientos T
				set T.descripcion = descripcion,
					T.fechaInicio = fechaInicio,
					T.fechaFin = fechaFin,
					T.medicamentosIndicados = medicamentosIndicados,
					T.codigoConsulta = codigoConsulta
						where T.codigoTratamiento = codigoTratamiento;
        End //
Delimiter ;
call sp_EditarTratamiento(2, 'Fractura en pata', '2025-04-02', '2025-04-12', 'Analgésicos y yeso', 2);
call sp_EditarTratamiento(7, 'Vacunación', '2025-04-07', '2025-04-17', 'Vacuna de la rabia', 7);

--  PROCEDIMIENTOS ALMACENADOS DE CITAS
-- Agregar Cita
Delimiter //
	Create procedure sp_AgregarCita(
    in fechaCita date, 
    in horaCita time, 
    in motivo varchar(255), 
    in codigoMascota int, 
    in codigoVeterinario int, 
    in estado enum('Pendiente','Completa','Cancelada'))
		Begin
			Insert into Citas(fechaCita, horaCita, motivo, codigoMascota, codigoVeterinario, estado)
				Values(fechaCita, horaCita, motivo, codigoMascota, codigoVeterinario, estado);
        End //
Delimiter ;
call sp_AgregarCita('2025-04-01', '10:00:00', 'Chequeo general', 1, 1, 'Pendiente');
call sp_AgregarCita('2025-04-02', '11:15:00', 'Revisión de vacunación', 2, 2, 'Completa');
call sp_AgregarCita('2025-04-03', '09:30:00', 'Lesión en pata', 3, 3, 'Pendiente');
call sp_AgregarCita('2025-04-04', '12:00:00', 'Control de peso', 4, 4, 'Pendiente');
call sp_AgregarCita('2025-04-05', '14:00:00', 'Chequeo dental', 1, 1, 'Completa');
call sp_AgregarCita('2025-04-06', '10:30:00', 'Vacunación', 2, 2, 'Pendiente');
call sp_AgregarCita('2025-04-07', '08:45:00', 'Revisión estomacal', 7, 7, 'Completa');
call sp_AgregarCita('2025-04-08', '11:00:00', 'Chequeo general', 8, 8, 'Pendiente');
call sp_AgregarCita('2025-04-09', '16:00:00', 'Examen de piel', 9, 9, 'Pendiente');
call sp_AgregarCita('2025-04-10', '13:00:00', 'Control de peso', 10, 10, 'Pendiente');
call sp_AgregarCita('2025-04-11', '15:30:00', 'Chequeo general', 11, 11, 'Pendiente');
call sp_AgregarCita('2025-04-12', '09:30:00', 'Revisión de alergias', 12, 12, 'Pendiente');
call sp_AgregarCita('2025-04-13', '10:15:00', 'Examen general', 13, 13, 'Pendiente');
call sp_AgregarCita('2025-04-14', '12:45:00', 'Chequeo dental', 14, 14, 'Pendiente');
call sp_AgregarCita('2025-04-15', '14:30:00', 'Chequeo general', 15, 15, 'Pendiente');

-- Listar Citas
Delimiter //
	Create procedure sp_ListarCitas()
		Begin
			Select codigoCita, fechaCita, horaCita, motivo, codigoMascota, codigoVeterinario, estado from Citas;
        End //
Delimiter ;
 call sp_ListarCitas();

-- Eliminar Citas
Delimiter //
	Create procedure sp_EliminarCita(
    in _codigoCita int)
	Begin
        set foreign_key_checks = 0;
			Delete from Citas
				where codigoCita = _codigoCita;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
call sp_EliminarCita(5);
call sp_EliminarCita(6);

-- Buscar Consulta
Delimiter //
	Create procedure sp_BuscarCita(
    in _codigoCita int)
		Begin
			Select codigoCita, fechaCita, horaCita, motivo, codigoMascota, codigoVeterinario, estado from Citas
				where codigoCita = _codigoCita;
        End //
Delimiter ;
call sp_BuscarCita(3);

-- Editar Cita
Delimiter //
	Create procedure sp_EditarCita(
    in codigoCita int,
    in fechaCita date, 
    in horaCita time, 
    in motivo varchar(255), 
    in codigoMascota int, 
    in codigoVeterinario int, 
    in estado enum('Pendiente','Completa','Cancelada'))
		Begin
			Update Citas C
				set C.fechaCita = fechaCita,
					C.horaCita = horaCita,
					C.motivo = motivo,
					C.codigoMascota = codigoMascota,
					C.codigoVeterinario = codigoVeterinario,
					C.estado = estado
						where C.codigoCita = codigoCita;
        End //
Delimiter ;
call sp_EditarCita(2, '2025-04-02', '11:15:00', 'Revisión de vacunación', 2, 2, 'Completa');
call sp_EditarCita(7, '2025-04-07', '08:45:00', 'Revisión estomacal', 7, 7, 'Completa');

-- PROCEDIMIENTOS ALMACENADOS DE VACUNACION
-- Agregar Vacunacion
Delimiter //
	Create procedure sp_AgregarVacunacion(
    in fechaAplicacion date,
    in observaciones varchar(255),
    in codigoMascota int,
    in codigoVacuna int, 
    in codigoVeterinario int)
		Begin
			Insert into Vacunaciones( fechaAplicacion, observaciones, codigoMascota, codigoVacuna, codigoVeterinario)
				Values( fechaAplicacion, observaciones, codigoMascota, codigoVacuna, codigoVeterinario);
        End //
Delimiter ;
call sp_AgregarVacunacion('2025-04-01', 'Primera dosis antirrábica', 1, 1, 1);
call sp_AgregarVacunacion('2025-04-02', 'Refuerzo de parvovirus', 2, 2, 2);
call sp_AgregarVacunacion('2025-04-03', 'Vacuna contra moquillo', 3, 3, 3);
call sp_AgregarVacunacion('2025-04-04', 'Vacuna triple felina', 4, 4, 4);
call sp_AgregarVacunacion('2025-04-05', 'Vacuna antirrábica anual', 1, 1, 2);
call sp_AgregarVacunacion('2025-04-06', 'Primera dosis contra hepatitis', 1, 2, 1);
call sp_AgregarVacunacion('2025-04-07', 'Vacuna contra leptospirosis', 7, 7, 7);
call sp_AgregarVacunacion('2025-04-08', 'Vacuna de refuerzo general', 8, 8, 8);
call sp_AgregarVacunacion('2025-04-09', 'Vacuna para tos de perrera', 9, 9, 9);
call sp_AgregarVacunacion('2025-04-10', 'Vacuna contra moquillo', 10, 10, 10);
call sp_AgregarVacunacion('2025-04-11', 'Refuerzo antirrábico', 11, 11, 11);
call sp_AgregarVacunacion('2025-04-12', 'Vacuna contra parvovirus', 12, 12, 12);
call sp_AgregarVacunacion('2025-04-13', 'Vacuna triple', 13, 13, 13);
call sp_AgregarVacunacion('2025-04-14', 'Vacuna para felinos', 14, 14, 14);
call sp_AgregarVacunacion('2025-04-15', 'Refuerzo general', 15, 15, 15);

-- Listar Medicamentos
Delimiter //
	Create procedure sp_ListarVacunaciones()
		Begin
			Select codigoVacunacion, fechaAplicacion, observaciones, codigoMascota, codigoVacuna, codigoVeterinario from Vacunaciones;
        End //
Delimiter ;
call sp_ListarVacunaciones();

-- Eliminar Vacunaciones
Delimiter //
	Create procedure sp_EliminarVacunacion(
    in _codigoVacunacion int)
	Begin
        set foreign_key_checks = 0;
			Delete from Vacunaciones
				where codigoVacunacion = _codigoVacunacion;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
call sp_EliminarVacunacion(5);
call sp_EliminarVacunacion(6);

-- Buscar Vacunacion
Delimiter //
	Create procedure sp_BuscarVacunacion(
    in _codigoVacunacion int)
		Begin
			Select codigoVacunacion, fechaAplicacion, observaciones, codigoMascota, codigoVacuna, codigoVeterinario from Vacunaciones
				where codigoVacunacion = _codigoVacunacion;
        End //
Delimiter ;
call sp_BuscarVacunacion(3);

-- Editar Vacunacion
Delimiter //
	Create procedure sp_EditarVacunacion(
    in codigoVacunacion int,
    in fechaAplicacion date,
    in observaciones varchar(255),
    in codigoMascota int,
    in codigoVacuna int, 
    in codigoVeterinario int)
		Begin
			Update Vacunaciones V
				set V.fechaAplicacion = fechaAplicacion,
					V.observaciones = observaciones,
                    V.codigoMascota = codigoMascota,
                    V.codigoVacuna = codigoVacuna,
                    V.codigoVeterinario = codigoVeterinario
						where V.codigoVacunacion = codigoVacunacion ;
        End //
Delimiter ;
call sp_EditarVacunacion(2, '2025-04-02', 'Refuerzo de parvovirus modificado', 2, 2, 2);
call sp_EditarVacunacion(7, '2025-04-07', 'Leptospirosis actualizada', 7, 7, 7);

-- PROCEDIMIENTOS ALMACENADOS DE MEDICAMENTOS
-- Agregar Medicamento
Delimiter //
	Create procedure sp_AgregarMedicamento(
    in nombre varchar(100), 
    in descripcion varchar(255), 
    in stock int, 
    in precioUnitario decimal(10,2), 
    in fechaVencimiento date, 
    in codigoProveedor int)
		Begin
			Insert into Medicamentos(nombre, descripcion, stock, precioUnitario, fechaVencimiento, codigoProveedor)
				Values(nombre, descripcion, stock, precioUnitario, fechaVencimiento, codigoProveedor);
        End //
Delimiter ;
call sp_AgregarMedicamento('Amoxicilina', 'Antibiótico de amplio espectro', 100, 12.50, '2025-12-01', 1);
call sp_AgregarMedicamento('Omeprazol', 'Protector gástrico', 80, 8.30, '2025-11-15', 2);
call sp_AgregarMedicamento('Pyrantel', 'Desparasitante', 150, 5.75, '2026-01-10', 3);
call sp_AgregarMedicamento('Metronidazol', 'Antibacteriano y antiparasitario', 200, 6.90, '2025-09-30', 4);
call sp_AgregarMedicamento('Prednisolona', 'Antiinflamatorio', 60, 9.40, '2025-10-20', 2);
call sp_AgregarMedicamento('Carprofeno', 'Antiinflamatorio no esteroide', 90, 11.20, '2026-02-05', 1);
call sp_AgregarMedicamento('Ketoconazol', 'Antifúngico', 40, 7.10, '2025-08-12', 7);
call sp_AgregarMedicamento('Ivermectina', 'Antiparasitario', 120, 4.85, '2025-07-01', 8);
call sp_AgregarMedicamento('Meloxicam', 'Antiinflamatorio', 50, 10.25, '2025-06-22', 9);
call sp_AgregarMedicamento('Doxiciclina', 'Antibiótico de amplio espectro', 70, 13.60, '2026-01-30', 10);
call sp_AgregarMedicamento('Enrofloxacina', 'Antibiótico potente', 30, 14.90, '2025-08-25', 11);
call sp_AgregarMedicamento('Clindamicina', 'Antibiótico para infecciones óseas', 45, 9.80, '2025-09-05', 12);
call sp_AgregarMedicamento('SulfaTrim', 'Combinación de antibióticos', 110, 7.50, '2026-03-15', 13);
call sp_AgregarMedicamento('Ranitidina', 'Inhibidor de ácido estomacal', 85, 6.25, '2025-05-30', 14);
call sp_AgregarMedicamento('Furosemida', 'Diurético', 95, 4.90, '2026-04-10', 15);

-- Listar Medicamentos
Delimiter //
	Create procedure sp_ListarMedicamentos()
		Begin
			Select codigoMedicamento, nombre, descripcion, stock, precioUnitario, fechaVencimiento from Medicamentos;
        End //
Delimiter ;
 call sp_ListarMedicamentos();

-- Eliminar Medicamento
Delimiter //
	Create procedure sp_EliminarMedicamento(
    in _codigoMedicamento int)
	Begin
        set foreign_key_checks = 0;
			Delete from Medicamentos
				where codigoMedicamento = _codigoMedicamento;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
 call sp_EliminarMedicamento(5);
 call sp_EliminarMedicamento(6);

-- Buscar Medicamento
Delimiter //
	Create procedure sp_BuscarMedicamento(
    in _codigoMedicamento int)
		Begin
			Select codigoMedicamento, nombre, descripcion, stock, precioUnitario, fechaVencimiento from Medicamentos
				where codigoMedicamento = _codigoMedicamento;
        End //
Delimiter ;
call sp_BuscarMedicamento(3);

-- Editar Medicamento
Delimiter //
	Create procedure sp_EditarMedicamento(
    in codigoMedicamento int,
    in nombre varchar(100), 
    in descripcion varchar(255), 
    in stock int, 
    in precioUnitario decimal(10,2), 
    in fechaVencimiento date, 
    in codigoProveedor int)
		Begin
			Update Medicamentos M
				set M.nombre = nombre,
					M.descripcion = descripcion,
					M.stock = stock,
					M.precioUnitario = precioUnitario,
					M.fechaVencimiento = fechaVencimiento,
					M.codigoProveedor = codigoProveedor
						where M.codigoMedicamento = codigoMedicamento;
        End //
Delimiter ;
call sp_EditarMedicamento(2, 'Omeprazol', 'Protector gástrico modificado', 75, 8.50, '2025-11-20', 2);
call sp_EditarMedicamento(7, 'Ketoconazol', 'Antifúngico en crema', 35, 7.50, '2025-08-15', 7);

-- PROCEDIMIENTOS ALMACENADOS DE RECETAS
-- Agregar Recetas
Delimiter //
	Create procedure sp_AgregarReceta(
    in dosis varchar(100), 
    in frecuencia varchar(100), 
    in duracionDosis int, 
    in indicaciones varchar(255), 
    in codigoConsulta int, 
    in codigoMedicamento int)
		Begin
			Insert into Recetas (dosis, frecuencia, duracionDosis, indicaciones, codigoConsulta, codigoMedicamento)
				Values(dosis, frecuencia, duracionDosis, indicaciones, codigoConsulta, codigoMedicamento);
        End //
Delimiter ;
call sp_AgregarReceta('1 comprimido', 'Cada 8 horas', 7, 'Administrar con alimento', 1, 1);
call sp_AgregarReceta('5 ml', 'Cada 12 horas', 5, 'Agitar antes de usar', 2, 2);
call sp_AgregarReceta('2 tabletas', 'Cada 24 horas', 10, 'No combinar con otros medicamentos', 3, 3);
call sp_AgregarReceta('1 ml', 'Cada 6 horas', 3, 'Inyectable', 4, 4);
call sp_AgregarReceta('1 cucharada', 'Cada 8 horas', 7, 'Conservar en refrigeración', 1, 1);
call sp_AgregarReceta('3 gotas', 'Cada 12 horas', 14, 'Aplicar en los ojos', 2, 2);
call sp_AgregarReceta('1 comprimido', 'Cada 24 horas', 3, 'No administrar con leche', 7, 7);
call sp_AgregarReceta('5 ml', 'Cada 8 horas', 5, 'Diluir en agua', 8, 8);
call sp_AgregarReceta('2 ml', 'Cada 6 horas', 4, 'Uso exclusivo veterinario', 9, 9);
call sp_AgregarReceta('1 ml', 'Cada 12 horas', 10, 'Aplicación subcutánea', 10, 10);
call sp_AgregarReceta('1 cápsula', 'Cada 24 horas', 7, 'Vía oral', 11, 11);
call sp_AgregarReceta('4 gotas', 'Cada 8 horas', 3, 'Agitar antes de usar', 12, 12);
call sp_AgregarReceta('2 tabletas', 'Cada 12 horas', 5, 'No interrumpir el tratamiento', 13, 13);
call sp_AgregarReceta('10 ml', 'Cada 24 horas', 7, 'Evitar la exposición al sol', 14, 14);
call sp_AgregarReceta('1 pastilla', 'Cada 8 horas', 5, 'No mezclar con alimento', 15, 15);


-- Listar Recetas
Delimiter //
	Create procedure sp_ListarRecetas()
		Begin
			Select codigoReceta, dosis, frecuencia, duracionDosis, indicaciones, codigoConsulta, codigoMedicamento from Recetas;
        End //
Delimiter ;
 call sp_ListarRecetas();

-- Eliminar Recetas
Delimiter //
	Create procedure sp_EliminarReceta(
    in _codigoReceta int)
	Begin
        set foreign_key_checks = 0;
			Delete from Recetas
				where codigoReceta = _codigoReceta;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
call sp_EliminarReceta(5);
call sp_EliminarReceta(6);

-- Buscar Receta
Delimiter //
	Create procedure sp_BuscarReceta(
    in _codigoReceta int)
		Begin
			Select codigoReceta, dosis, frecuencia, duracionDosis, indicaciones, codigoConsulta, codigoMedicamento from Recetas
				where codigoReceta = _codigoReceta;
        End //
Delimiter ;
call sp_BuscarReceta(3);

-- Editar Receta
Delimiter //
	Create procedure sp_EditarReceta(
    in codigoReceta int,
	in dosis varchar(100), 
    in frecuencia varchar(100), 
    in duracionDosis int, 
    in indicaciones varchar(255), 
    in codigoConsulta int, 
    in codigoMedicamento int)
		Begin
			Update Recetas R
				set R.dosis = dosis,
					R.frecuencia = frecuencia,
					R.duracionDosis = duracionDosis,
					R.indicaciones = indicaciones,
					R.codigoConsulta = codigoConsulta,
					R.codigoMedicamento = codigoMedicamento
						where R.codigoReceta = codigoReceta;
        End //
Delimiter ;
call sp_EditarReceta(2, '5 ml', 'Cada 12 horas', 5, 'Agitar antes de usar - modificado', 2, 2);
call sp_EditarReceta(7, '1 comprimido', 'Cada 24 horas', 3, 'Modificado: No administrar con leche', 7, 7);


-- PROCEDIMIENTOS ALMACENADOS DE FACTURA
-- Agregar Factura
Delimiter //
	Create procedure sp_AgregarFactura(
    in fechaEmision date, 
    in total decimal(10,2), 
    in metodoPago enum('Efectivo','Tarjeta','Transferencia'), 
    in codigoCliente int, 
    in codigoEmpleado int)
		Begin
			Insert into Facturas(fechaEmision, total, metodoPago, codigoCliente, codigoEmpleado)
				Values(fechaEmision, total, metodoPago, codigoCliente, codigoEmpleado);
        End //
Delimiter ;
call sp_AgregarFactura('2025-04-01', 125.00, 'Efectivo', 1, 1);
call sp_AgregarFactura('2025-04-02', 98.50, 'Tarjeta', 2, 2);
call sp_AgregarFactura('2025-04-03', 67.75, 'Transferencia', 3, 3);
call sp_AgregarFactura('2025-04-04', 110.25, 'Efectivo', 4, 4);
call sp_AgregarFactura('2025-04-05', 220.40, 'Tarjeta', 1, 1);
call sp_AgregarFactura('2025-04-06', 134.60, 'Transferencia', 2, 2);
call sp_AgregarFactura('2025-04-07', 180.00, 'Efectivo', 7, 7);
call sp_AgregarFactura('2025-04-08', 95.30, 'Tarjeta', 8, 8);
call sp_AgregarFactura('2025-04-09', 145.90, 'Transferencia', 9, 9);
call sp_AgregarFactura('2025-04-10', 87.15, 'Efectivo', 10, 10);
call sp_AgregarFactura('2025-04-11', 190.80, 'Tarjeta', 11, 11);
call sp_AgregarFactura('2025-04-12', 160.50, 'Transferencia', 12, 12);
call sp_AgregarFactura('2025-04-13', 112.90, 'Efectivo', 13, 13);
call sp_AgregarFactura('2025-04-14', 75.00, 'Tarjeta', 14, 14);
call sp_AgregarFactura('2025-04-15', 138.35, 'Transferencia', 15, 15);
-- Listar Facturas
Delimiter //
	Create procedure sp_ListarFacturas()
		Begin
			Select codigoFactura, fechaEmision, total, metodoPago, codigoCliente, codigoEmpleado from Facturas;
        End //
Delimiter ;
call sp_ListarFacturas();

-- Eliminar Factura
Delimiter //
	Create procedure sp_EliminarFactura(
    in _codigoFactura int)
	Begin
        set foreign_key_checks = 0;
			Delete from Facturas
				where codigoFactura = _codigoFactura;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;

-- Buscar Factura
Delimiter //
	Create procedure sp_BuscarFactura(
    in _codigoFactura int)
		Begin
			Select codigoFactura, fechaEmision, total, metodoPago, codigoCliente, codigoEmpleado from Facturas
				where codigoFactura = _codigoFactura;
        End //
Delimiter ;
call sp_BuscarFactura(3);

-- Editar Factura
Delimiter //
	Create procedure sp_EditarFactura(
    in codigoFactura int,
    in fechaEmision date, 
    in total decimal(10,2), 
    in metodoPago enum('Efectivo','Tarjeta','Transferencia'), 
    in codigoCliente int, 
    in codigoEmpleado int)
		Begin
			Update Facturas F
				set F.fechaEmision = fechaEmision,
					F.total = total,
					F.metodoPago = metodoPago,
					F.codigoCliente = codigoCliente,
					F.codigoEmpleado = codigoEmpleado
						where F.codigoFactura = codigoFactura;
        End //
Delimiter ;
call sp_EditarFactura(2, '2025-04-02', 100.00, 'Tarjeta', 2, 2);
call sp_EditarFactura(7, '2025-04-07', 185.00, 'Efectivo', 7, 7);

-- PROCEDIMIENTOS ALMACENADOS DE COMPRAS
-- Agregar Compra
Delimiter //
	Create procedure sp_AgregarCompra(
    in fechaCompra date, 
    in total decimal(10,2), 
    in detalle varchar(255), 
    in codigoProveedor int)
		Begin
			Insert into Compras(fechaCompra, total, detalle, codigoProveedor)
				Values(fechaCompra, total, detalle, codigoProveedor);
        End //
Delimiter ;
call sp_AgregarCompra('2025-04-01', 340.00, 'Compra de vacunas', 1);
call sp_AgregarCompra('2025-04-02', 210.50, 'Medicamentos varios', 2);
call sp_AgregarCompra('2025-04-03', 450.00, 'Antibióticos y jeringas', 3);
call sp_AgregarCompra('2025-04-04', 390.75, 'Insumos generales', 4);
call sp_AgregarCompra('2025-04-05', 320.30, 'Compra de cremas y sprays', 1);
call sp_AgregarCompra('2025-04-06', 270.00, 'Suplementos y vitaminas', 2);
call sp_AgregarCompra('2025-04-07', 310.40, 'Vacunas felinas', 7);
call sp_AgregarCompra('2025-04-08', 299.90, 'Limpieza y desinfección', 8);
call sp_AgregarCompra('2025-04-09', 215.60, 'Pipetas antipulgas', 9);
call sp_AgregarCompra('2025-04-10', 305.25, 'Antiparasitarios y collares', 10);
call sp_AgregarCompra('2025-04-11', 398.90, 'Compra mensual', 11);
call sp_AgregarCompra('2025-04-12', 220.45, 'Medicinas urgentes', 12);
call sp_AgregarCompra('2025-04-13', 330.00, 'Reposición de stock', 13);
call sp_AgregarCompra('2025-04-14', 280.00, 'Alimentos especiales', 14);
call sp_AgregarCompra('2025-04-15', 365.10, 'Sueros y líquidos IV', 15);

-- Listar Compras
Delimiter //
	Create procedure sp_ListarCompras()
		Begin
			Select codigoCompra, fechaCompra, total, detalle, codigoProveedor from Compras;
        End //
Delimiter ;
call sp_ListarCompras();

-- Eliminar Factura
Delimiter //
	Create procedure sp_EliminarCompra(
    in _codigoCompra int)
	Begin
        set foreign_key_checks = 0;
			Delete from Compras
				where codigoCompra = _codigoCompra;
			Select row_count() as filasEliminadas;
		set foreign_key_checks = 1;
	End //
Delimiter ;
call sp_EliminarCompra(5);
call sp_EliminarCompra(6);
-- Buscar Compra
Delimiter //
	Create procedure sp_BuscarCompra(
    in _codigoCompra int)
		Begin
			Select codigoCompra, fechaCompra, total, detalle, codigoProveedor from Compras
				where codigoCompra = _codigoCompra;
        End //
Delimiter ;
call sp_BuscarCompra(3);

-- Editar Compra
Delimiter //
	Create procedure sp_EditarCompra(
    in codigoCompra int,
    in fechaCompra date, 
    in total decimal(10,2), 
    in detalle varchar(255), 
    in codigoProveedor int)
		Begin
			Update Compras C
				set C.fechaCompra = fechaCompra,
					C.total = total,
					C.detalle = detalle,
					C.codigoProveedor = codigoProveedor
						where C.codigoCompra = codigoCompra;
        End //
Delimiter ;
call sp_EditarCompra(2, '2025-04-02', 215.00, 'Medicamentos actualizados', 2);
call sp_EditarCompra(7, '2025-04-07', 315.00, 'Vacunas felinas modificadas', 7);


-- consultas para los reportes ejercicios ejemplo
-- ,Primer Reporte: La Receta indicada por el veterinario a la mascota. (Medicamentos, Dosis, Cuanto Tiempo y el Nombre de la Mascota)

delimiter //
	create procedure sp_RecetaMacota()
		begin
			select 
			M.nombre as 'Medicamento', R.dosis as 'Dosis',R.duracionDosis as 'Duracion de las dosis',
            MA.nombreMascota as 'Nombre de la mascota' 
			from Recetas R
			join Medicamentos M on R.codigoMedicamento = M.codigoMedicamento
			join Consultas C on R.codigoConsulta = C.codigoConsulta
			join Mascotas MA on C.codigoMascota = MA.codigoMascota;
		end //
delimiter ;
-- drop procedure sp_RecetaMacota;
call sp_RecetaMacota();

-- Segundo Reporte: Carné de Vacunación (Vacunas, Vacunación, Veterinario, Nombre de la Mascota y el Dueño)

Delimiter //
create procedure sp_CarneVacuancion()
	begin
		select 
		V.nombreVacuna as 'Nombre de la Vacuna', VA.fechaAplicacion as 'Fecha de Vacunacion', 
		concat(VE.nombreVeterinario, ' ', ve.apellidoVeterinario) as 'Veterinario',
		MA.nombreMascota as 'Nombre de la mascota',
		concat(C.nombreCliente, ' ', C.apellidoCliente) as 'Nombre del Dueño'
		from Vacunaciones VA
		join Vacunas V on VA.codigoVacuna = V.codigoVacuna
		join Veterinarios VE on VA.codigoVeterinario = VE.codigoVeterinario
		join Mascotas MA on VA.codigoMascota = MA.codigoMascota
		join Clientes C on MA.codigoCliente = C.codigoCliente;
    end //
Delimiter ;
call sp_CarneVacuancion();
-- Tercer Reporte: Factura (Facturas, Empleado quien atendio, Cliente y la Mascota)

Delimiter //
Create procedure sp_FacturaAtencion()
	begin
		select 
		F.codigoFactura,concat(E.nombreEmpleado, ' ', E.apellidoEmpleado) as 'Empleado',
		concat(C.nombreCliente, ' ', C.apellidoCliente) as 'Cliente', M.nombreMascota as 'Mascota'
		from Facturas F
		join Empleados E on F.codigoEmpleado = E.codigoEmpleado
		join Clientes C on F.codigoCliente = C.codigoCliente
		join Mascotas M on C.codigoCliente = M.codigoCliente;
    end //
Delimiter ;
call sp_FacturaAtencion();
