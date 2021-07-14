drop database if exists DBTonysKinal2017443;
create database DBTonysKinal2017443;
use DBTonysKinal2017443;


create table Empresas(
codigoEmpresa int not null auto_increment primary key,
nombreEmpresa varchar(150) not null,
direccion varchar(150) not null,
telefono varchar(10) not null
);

create table Servicios(
codigoServicio int not null auto_increment primary key,
fechaServicio date not null,
tipoServicio varchar(100) not null,
horaServicio time not null,
lugarServicio varchar(100) not null,
telefonoContacto varchar(10) not null,
codigoEmpresa int,
constraint FK_Servicios_Empresas foreign key (codigoEmpresa) references Empresas (codigoEmpresa) on delete cascade

);

create table TipoEmpleado(
codigoTipoEmpleado int not null auto_increment primary key,
descripcion varchar(150) not null
);

create table Empleados(
codigoEmpleado int not null auto_increment primary key,
numeroEmpleado int not null,
apellidoEmpleado varchar(150) not null,
nombreEmpleado varchar(150) not null,
direccionEmpleado varchar(150) not null,
telefonoContacto varchar(150) not null,
gradoCocinero varchar(150),
codigoTipoEmpleado int not null,
constraint FK_Empleados_TipoEmpleado foreign key(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade
);

create table Servicios_has_Empleados(
codigoServicio int not null,
codigoEmpleado int not null,
fechaEvento date not null,
horaEvento time not null,
lugarEvento varchar(150)not null,
primary key (codigoServicio, codigoEmpleado),
constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado) references Empleados(codigoEmpleado) on delete cascade
);
	alter table servicios_has_empleados add Numero int(250) auto_increment unique first;
    
create table TipoPlato(
codigoTipoPlato int not null auto_increment primary key,
descripcion varchar(150) not null
);

create table Platos(
codigoPlato int not null auto_increment primary key,
cantidad int not null,
nombrePlato varchar(150) not null,
descripcionPlato varchar(150) not null,
precioPlato decimal(10,2) not null,
codigoTipoPlato int not null ,
constraint FK_Platos_TipoPlato foreign key(codigoTipoPlato) references TipoPlato(codigoTipoPlato) on delete cascade 

);

create table Productos(
codigoProducto int not null auto_increment primary key,
nombreProducto varchar(150) not null,
cantidad int not null
);

create table Productos_has_Platos(
Productos_codigoProducto int not null, 
Platos_codigoPlato int not null,
primary key (Productos_codigoProducto, Platos_codigoPlato),
constraint FK_Productos_has_Platos_Productos foreign key (Productos_codigoProducto) references Productos(codigoProducto),
constraint FK_Productos_has_Platos_Platos foreign key (Platos_codigoPlato) references Platos(codigoPlato) 
);

create table Servicios_has_Platos(
codigoServicio int not null,
codigoPlato int not null,
primary key (codigoServicio, codigoPlato),
constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato) references Platos(codigoPlato) on delete cascade
);

create table Presupuesto(
codigoPresupuesto int not null auto_increment primary key,
fechaSolicitud date not null,
cantidadPresupuesto decimal(10,2) not null,
codigoEmpresa int not null,
constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade
);

/**************************************PROCEDIMINETO_PRODUCTOS*********************************************/
Delimiter $$
create procedure sp_AgregarProductos(
in codigoProducto int, 
in nombreProducto varchar(150),
in cantidad int
)
	begin 
		insert into Productos(codigoProducto, nombreProducto, cantidad)
        values ( codigoProducto, nombreProducto, cantidad); 
	end $$
Delimiter ;
call sp_AgregarProductos(1,'Verduras y carnes',3);
call sp_AgregarProductos(2,'Pastas y salsas',5);
call sp_AgregarProductos(3,'Barbarcoa',7);
call sp_AgregarProductos(4,'Variedad de verduras',2);
call sp_AgregarProductos(5, 'Miga de pan y huevos',5);
call sp_AgregarProductos(6,'Crema, pollo y papa',4);

				/****************ActualizarProductos*****************/
Delimiter $$
create procedure sp_ActualizarProductos(
in _codigoProducto int,
in _nombreProducto varchar(150),
in _cantidad int
)
	begin 
		update productos set
        nombreProducto=_nombreProducto,
        cantidad=_cantidad
        where codigoProducto=_codigoProducto;
	end $$
Delimiter ; 
				/****************ListarProductos*****************/
Delimiter $$
create procedure sp_ListarProductos()
	begin
		select 
			Productos.codigoProducto,
            Productos.nombreProducto,
            Productos.cantidad 
				from Productos;
	end $$
Delimiter ;

/*call sp_ListarProductos;*/
				/****************EliminarProductos*****************/
Delimiter $$
create procedure sp_EliminarProductos(
in _codigoProducto int
)
	begin
		delete from Productos where codigoProducto = _codigoProducto;
    end $$
Delimiter ;

/*call sp_EliminarProductos();*/

				/****************BuscarProductos*****************/
Delimiter $$
create procedure sp_BuscarProductos(
in _codigoProducto int
)
	begin
			select 
				Productos.codigoProducto,
                Productos.nombreProducto,
                Productos.cantidad 
					from Productos where codigoProducto = _codigoProducto;
	end $$
Delimiter ;

/*call sp_BuscarProductos(2);*/

/*********************************PROCEDIMINETO_TIPOPLATO*************************************************/
Delimiter $$
create procedure sp_AgregarTipoPlato(
in codigoTipoPlato int,
in descripcion varchar(150)
)
	begin
			insert into tipoPlato(codigoTipoPlato,descripcion)
				values ( codigoTipoPlato, descripcion);
	end $$
Delimiter ;

call sp_AgregarTipoPlato(1,'italiano verche');
call sp_AgregarTipoPlato(2,'plato de pan');
call sp_AgregarTipoPlato(3,'plato presentacion');
call sp_AgregarTipoPlato(4,'plato de Caldo');
call sp_AgregarTipoPlato(5,'plato sencillo');
call sp_AgregarTipoPlato(6,'Plato elegante');
				/****************ActualizarTipoPlato*****************/
Delimiter $$
create procedure sp_ActualizarTipoPlato(
in _codigoTipoPlato int,
in _descripcion varchar(150)
)
	begin
		update TipoPlato set
				descripcion = _descripcion
                where codigoTipoPlato= _codigoTipoPlato;
    end $$
Delimiter ;

/*call sp_ActualizarTipoPlato(2,'Plato de pan.');*/

				/****************ListarTipoPlato*****************/
Delimiter $$
create procedure sp_ListarTipoPlatos()
	begin
		select 
			TipoPlato.codigoTipoPlato,
            TipoPlato.descripcion
				from TipoPlato;
	end $$
Delimiter ;

				/****************EliminarTipoPlato*****************/
Delimiter $$
create procedure sp_EliminarTipoPlato(
in _codigoTipoPlato int
)
	begin
		delete from TipoPlato where codigoTipoPlato = _codigoTipoPlato;
	end $$
Delimiter ;

/*call sp_EliminarTipoPlato(6);*/

				/****************BuscarTipoPlato*****************/
Delimiter $$
create procedure sp_BuscarTipoPlato(
in _codigoTipoPlato int
)
	begin
		select 
			TipoPlato.codigoTipoPlato,
            TipoPlato.descripcion
				from TipoPlato where codigoTipoPlato = _codigoTipoPlato;
	end $$
Delimiter ;

/*call sp_BuscarTipoPlato(2);*/

/************************************PROCEDIMIENTO_PLATO******************************************/
Delimiter $$
create procedure sp_AgregarPlatos(
in codigoPlato int,
in cantidad int,
in nombrePlato varchar(150),
in descripcionPlato varchar(150),
in precioPlato decimal(10,2),
in codigoTipoPlato int
)
	begin
		insert into Platos(codigoPlato,cantidad,nombrePlato,descripcionPlato,precioPlato,codigoTipoPlato)
			values ( codigoPlato, cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
	end $$	
Delimiter ;

call sp_AgregarPlatos(1,4,'caldo de res','caldos tradiciones',18,1);
call sp_AgregarPlatos(2,11,'Lazaña','salsa y albondigas',14,2);
call sp_AgregarPlatos(3,16,'polloBarbacoa','azados',11,3);
call sp_AgregarPlatos(4,12,'caldo Tipico de res','caldos',15,4);
call sp_AgregarPlatos(5,7,'Milanesa','Envuelto',15,5);
call sp_AgregarPlatos(6,6,'Alitas en crema','pollo',25,3);
selec
				/****************ActualizarPlatos*****************/
Delimiter $$
create procedure sp_ActualizarPlatos(
in _codigoPlato int,
in _cantidad int,
in _nombrePlato varchar(150),
in _descripcionPlato varchar(150),
in _precioPlato decimal(10,2),
in _codigoTipoPlato int
)
	begin
		update Platos set
            cantidad = _cantidad,
            nombrePlato = _nombrePlato,
            descripcionPlato = _descripcionPlato,
            precioPlato = _precioPlato,
            codigoTipoPlato = _codigoTipoPlato
				where codigoPlato = _codigoPlato;
	end $$
Delimiter ;

/*call sp_ActualizarPlatos(4,12,'caldo Tipico de res','caldos',15,4);*/          
 
				/****************ActualizarPlatos*****************/
Delimiter $$
create procedure sp_ListarPlatos()
	begin
		select 
			Platos.codigoPlato,
            Platos.cantidad,
            Platos.nombrePlato,
            Platos.descripcionPlato,
            Platos.precioPlato,
            Platos.codigoTipoPlato from Platos;
    end $$
Delimiter ;
				/****************EliminarrPlatos*****************/
Delimiter $$
create procedure sp_EliminarPlatos(
in _codigoPlato int
)
	begin
		delete from Platos where codigoPlato = _codigoPlato;
	end $$
Delimiter ;

/*all sp_EliminarPlatos(5);*/

				/****************BuscarrPlatos*****************/
Delimiter $$
create procedure sp_BuscarPlatos(
in _codigoPlato int
)
	begin
		select
			Platos.codigoPlato,
            Platos.cantidad,
            Platos.nombrePlato,
            Platos.descripcionPlato,
            Platos.precioPlato,
            Platos.codigoTipoPlato from Platos where codigoPlato = _codigoPlato;
			
	end $$
Delimiter ;
/*call sp_BuscarPlatos(3);*/


/***************************¡******PRODUCTOS_HAS_PLATOS********************************************/
Delimiter $$
create procedure sp_AgregarProductos_has_Platos(
in Productos_codigoProducto int,
in Platos_codigoPlato int
)
	begin
		insert into Productos_has_Platos(Productos_codigoProducto,Platos_codigoPlato)
			values (Productos_codigoProducto, Platos_codigoPlato);
	end $$
Delimiter ;
call sp_AgregarProductos_has_Platos(1,2);
call sp_AgregarProductos_has_Platos(2,2);
call sp_AgregarProductos_has_Platos(2,3);
call sp_AgregarProductos_has_Platos(3,3);

				/****************Actualizar*****************/
Delimiter $$
create procedure sp_ActualizarProductos_has_Platos(
in _Productos_codigoProducto int,
in _Platos_codigoPlato  int
)
	begin
		update Productos_has_Platos set 
            Platos_codigoPlato = _Platos_codigoPlato
				where Productos_codigoProducto = _Productos_codigoProducto;
	end $$
Delimiter ;
/*call sp_ActualizarProductos_has_Platos(2,2);*/


				/****************Listar*****************/
Delimiter $$
create procedure sp_ListarProductos_has_Platos()
	begin
		select 
			productos_has_platos.Productos_codigoProducto,
            productos_has_platos.Platos_codigoPlato
				from productos_has_platos;
	end $$
Delimiter ;
				/****************Eliminar*****************/
Delimiter $$ 
create procedure sp_EliminarProductos_has_Platos(
in _Productos_codigoProducto int
)
	begin
		delete from productos_has_platos where Productos_codigoProducto = _Productos_codigoProducto;
	end $$
Delimiter ;

				/****************BUSCAR*****************/
Delimiter $$
create procedure sp_BuscarProductos_has_Platos(
in _Productos_codigoProducto int 
)
	begin
		select 
			productos_has_platos.Productos_codigoProducto,
            productos_has_platos.Platos_codigoPlato
				from productos_has_platos where Productos_codigoProducto = _Productos_codigoProducto;
	end $$
Delimiter ;
/*call sp_BuscarProductos_has_Platos(3);*/

/**********************************PROCEDIMIENTO_EMPRESAS*************************************************/
Delimiter $$
create procedure sp_AgregarEmpresas(
in codigoEmpresa int,
in nombreEmpresa varchar(150),
in direccion varchar (150),
in telefono varchar(150)
)
	begin
		insert into Empresas(codigoEmpresa,nombreEmpresa,direccion,telefono)
			values ( codigoEmpresa, nombreEmpresa, direccion, telefono);
	end $$
Delimiter ;

call sp_AgregarEmpresas(1,'Ivanox','7 av 30 calles zona 12','23455622');
call sp_AgregarEmpresas(2,'El Injerto cafe','km 18 ruta al el salbador','12113455');
call sp_AgregarEmpresas(3,'Agrocaribe','37av 13 calle zona 5','12334889');
call sp_AgregarEmpresas(4,'Summary','3av 6 calle zona 8','00998877');
call sp_AgregarEmpresas(5,'NOTE','2av 7 calle zona 9','99884578');
call sp_AgregarEmpresas(6,'Atento','10av 6 calle Aguilar Batres zona 12','99884578');
call sp_AgregarEmpresas(7,'Piche','34av 9 calle La Reformita','99884578');
call sp_AgregarEmpresas(8,'Wendys','11av 2 calle Amatitlan','99884578');


				/****************ActualizarEmpresas*****************/
Delimiter $$
create procedure sp_ActualizarEmpresas(
in _codigoEmpresa int,
in _nombreEmpresa varchar(150),
in _direccion varchar (150),
in _telefono varchar(150)
)
	begin
		update Empresas set
			nombreEmpresa = _nombreEmpresa,
            direccion = _direccion,
            telefono = _telefono
				where codigoEmpresa = _codigoEmpresa;
    end $$
Delimiter ;

/*call sp_ActualizarEmpresas(9,'Atento','10av 6 calle Aguilar Batres zona 15','6988-5678');*/

				/****************ListarEmpresas*****************/
Delimiter $$
create procedure sp_ListarEmpresas()
	begin
		select 
			Empresas.codigoEmpresa,
            Empresas.nombreEmpresa,
            Empresas.direccion,
            Empresas.telefono 
				from Empresas;
	end $$
Delimiter ;
				/****************EliminarEmpresas*****************/
Delimiter $$
create procedure sp_EliminarEmpresas(
in _codigoEmpresa int
)
	begin
		delete from Empresas where codigoEmpresa = _codigoEmpresa;
	end $$
Delimiter ;

/*call sp_EliminarEmpresas(9);*/
				/****************BuscarEmpresas*****************/
Delimiter $$
create procedure sp_BuscarEmpresas(
in _codigoEmpresa int
)
	begin
		select 
			Empresas.codigoEmpresa,
            Empresas.nombreEmpresa,
            Empresas.direccion,
            Empresas.telefono 
				from Empresas where codigoEmpresa = _codigoEmpresa;
	end $$	
Delimiter ;

/*call sp_BuscarEmpresas(2);*/
/*ALTER USER 'root'@'localhost' identified WITH mysql_native_password BY 'admin';*/
/********************************PROCEDIMIENTO_SERVICIOS******************************************/
Delimiter $$
create procedure sp_AgregarServicios(
in codigoServicio int,
in fechaServicio date,
in tipoServicio varchar(100),
in horaServicio time,
in lugarServicio varchar(100),
in telefonoContacto varchar(11),
in codigoEmpresa int
)
	begin
		insert into Servicios(codigoServicio,fechaServicio,tipoServicio,horaServicio,lugarServicio,telefonoContacto,codigoEmpresa)
			values ( codigoServicio, fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
	end  $$
Delimiter ;

call sp_AgregarServicios(1,'10-03-20','AutoServicio','13:00','Aguilar Batres','11223367',1);
call sp_AgregarServicios(2,'20,10,11','A la francesa', '15:30','Unidos por la paz','12455566',2);
call sp_AgregarServicios(3,'20-05-10','Emplatado','17:15','Mixco','12335582',2);
call sp_AgregarServicios(4,'20-06-20','Americano','17:15','6av zona 1','12335582',2);
call sp_AgregarServicios(5,'20-06-20','A la francesa','17:15','Villa Hermosa','12335582',1);


				/****************ActualizarServicios*****************/
Delimiter $$
create procedure sp_ActualizarServicios(
in _codigoServicio int,
in _fechaServicio date,
in _tipoServicio varchar(100),
in _horaServicio time,
in _lugarServicio varchar(100),
in _telefonoContacto varchar(11),
in _codigoEmpresa int
)
	begin
		update Servicios set
            fechaServicio = _fechaServicio,
            tipoServicio = _tipoServicio,
            horaServicio = _horaServicio,
            lugarServicio = _lugarServicio,
            telefonoContacto = _telefonoContacto,
            codigoEmpresa = _codigoEmpresa
				where codigoServicio = _codigoServicio;
    end $$
Delimiter ;

/*call sp_ActualizarServicios(2,'05,10,11','A la francesa', '11:30','La Reformita','12455566',2);*/

				/****************ListarServicios*****************/
Delimiter $$
create procedure sp_ListarServicios()
	begin
		select
			Servicios.codigoServicio,
            Servicios.fechaServicio,
            Servicios.tipoServicio,
            Servicios.horaServicio,
            Servicios.lugarServicio,
            Servicios.telefonoContacto,
            Servicios.codigoEmpresa
            
				from servicios;
            
	end $$
Delimiter ;
				/****************EliminarServicios*****************/
Delimiter $$
create procedure sp_EliminarServicios(
in _codigoServicio int
)
	begin
		delete from Servicios where codigoServicio = _codigoServicio;
	end $$
Delimiter ;
/*call sp_EliminarServicios(4);*/

				/****************BuscarrServicios*****************/
Delimiter $$
create procedure sp_BuscarServicios(
in _codigoServicio int
)
	begin
		select
			Servicios.codigoServicio,
            Servicios.fechaServicio,
            Servicios.tipoServicio,
            Servicios.horaServicio,
            Servicios.lugarServicio,
            Servicios.telefonoContacto,
            Servicios.codigoEmpresa
				from servicios where codigoServicio = _codigoServicio;
    end $$
Delimiter ;

/*call sp_BuscarServicios(3);*/
/*********************************Servicios_Has_Platos*****************************************/
Delimiter $$
create procedure sp_Servicios_has_platos(
in codigoServicio int,
in codigoPlato int
)
	begin
		insert into servicios_has_platos(codigoServicio,codigoPlato)
			values ( codigoServicio, codigoPlato);
    end $$
Delimiter ;

call sp_Servicios_has_platos(1,1);
call sp_Servicios_has_platos(1,6);
call sp_Servicios_has_platos(3,1);
call sp_Servicios_has_platos(1,2);
call sp_Servicios_has_platos(1,3);
call sp_Servicios_has_platos(2,4);
call sp_Servicios_has_platos(2,5);
call sp_Servicios_has_platos(2,1);
call sp_Servicios_has_Platos(3,4);

				/****************Actualizar*****************/
/*Delimiter $$
create procedure sp_ActualizarServicios_has_Platos(
in _Servicios_codigoServicio int,
in _Platos_codigoPlato int
)
	begin
		update Servicios_has_Platos set
			Platos_codigoPlato = _Platos_codigoPlato
			where Servicios_codigoServicio = _Servicios_codigoServicio;
            
    end $$
Delimiter ;*/
				/****************Listar*****************/
Delimiter $$
create procedure sp_ListarServicios_has_Platos()
	begin
		select 
			servicios_has_platos.codigoServicio,
            servicios_has_platos.codigoPlato
				from servicios_has_platos;
	end $$
Delimiter ;
				/****************Eliminar*****************/
Delimiter $$
create procedure sp_EliminarServicios_has_Platos(
in _codigoServicio int
)
	begin
		delete from servicios_has_platos where codigoServicio = _codigoServicio;
	end $$
Delimiter ;

				/****************Buscar*****************/
/*Delimiter $$
create procedure sp_BuscarServicios_has_Platos(
in _Servicios_codigoServicio int
)
	begin
		select 
			servicios_has_platos.codigoServicio,
            servicios_has_platos.codigoPlato
				from servicios_has_platos where Servicios_codigoServicio = _Servicios_codigoServicio;
	end $$
Delimiter ;*/

/*call sp_BuscarServicios_has_Platos(1);*/
 /****************************************PRESUPUESTO*********************************************/
 Delimiter $$
 create procedure sp_AgregarPresupuesto(
 in codigoPresupuesto int,
 in fechaSolicitud date,
 in cantidadPresupuesto decimal(10,2),
 in codigoEmpresa int
 )
	begin
		insert into Presupuesto(codigoPresupuesto,fechaSolicitud,cantidadPresupuesto,codigoEmpresa)
			values (codigoPresupuesto, fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
	end $$
 Delimiter ;

call  sp_AgregarPresupuesto(1,'20-11-10',800,1);
call  sp_AgregarPresupuesto(2,'20-09-02',650,2);
call  sp_AgregarPresupuesto(3,'21-01-17',1000,1);
call  sp_AgregarPresupuesto(4,'20-02-12',2334,2);
call  sp_AgregarPresupuesto(5,'20-04-11',4556,3);
call  sp_AgregarPresupuesto(6,'20-04-11',4556,3);
call  sp_AgregarPresupuesto(7,'20-05-12',4556,2); 
				/****************ACTUALIZARPRESUPUESTO*****************/
Delimiter $$
create procedure sp_ActualizarPresupuesto(
in _codigoPresupuesto int,
in _fechaSolicitud date,
in _cantidadPresupuesto decimal(10,2),
in _codigoEmpresa int
 
)
	begin
		update Presupuesto set
			fechaSolicitud = _fechaSolicitud,
            cantidadPresupuesto = _cantidadPresupuesto,
            codigoEmpresa = _codigoEmpresa
				where codigoPresupuesto = _codigoPresupuesto;
    end $$
Delimiter ;

/*call sp_ActualizarPresupuesto(3,'20-04-15',600,1);*/

				/****************ACTUALIZARPRESUPUESTO*****************/
Delimiter $$
create procedure sp_ListarPresupuesto()
	begin
		select 
			Presupuesto.codigoPresupuesto,
            Presupuesto.fechaSolicitud,
            Presupuesto.cantidadPresupuesto,
            Presupuesto.codigoEmpresa
            
				from Presupuesto;
	end $$
Delimiter ;

/*call sp_ListarPresupuesto();*/
				/****************ELIMINARPRESUPUESTO*****************/
Delimiter $$
create procedure sp_EliminarPresupuesto(
in _codigoPresupuesto int
)
	begin
		delete from Presupuesto where codigoPresupuesto = _codigoPresupuesto;
    end $$
Delimiter ;

/*call sp_EliminarPresupuesto(5);*/
				/****************BUSCARPRESUPUESTO*****************/
Delimiter $$
create procedure sp_BuscarPresupuesto(
in _codigoPresupuesto int
)
	begin
		select 
			Presupuesto.codigoPresupuesto,
            Presupuesto.fechaSolicitud,
            Presupuesto.cantidadPresupuesto,
            Presupuesto.codigoEmpresa
				from Presupuesto where codigoPresupuesto = _codigoPresupuesto;
	end $$
Delimiter ;
/*call sp_BuscarPresupuesto(1);*/
/**********************************PROCEDIMIENTO_TIPOEMPLEADO**************************************/
 Delimiter $$
 create procedure sp_AgregarTipoEmpleado(
 in codigoTipoEmpleado int,
 in descripcion varchar(150)
 )
	begin
		insert into TipoEmpleado(codigoTipoEmpleado,descripcion)
			values ( codigoTipoEmpleado, descripcion);
    end $$
 Delimiter ;
 
 call sp_AgregarTipoEmpleado(1,'formidable');
 call sp_AgregarTipoEmpleado(2,'audaz');
 call sp_AgregarTipoEmpleado(3,'iniciativo');
 call sp_AgregarTipoEmpleado(4,'Timido');
 
				/****************ACTUALIZARTIPOEMPLEADO*****************/
Delimiter $$
create procedure sp_ActualizarTipoEmpleado(
 in _codigoTipoEmpleado int,
 in _descripcion varchar(150)
)
	begin
		update TipoEmpleado set
            descripcion = _descripcion
            where codigoTipoEmpleado = _codigoTipoEmpleado;
	end $$
Delimiter ;
call  sp_ActualizarTipoEmpleado(4,'chispudo');

				/****************LISTARTIPOEMPLEADO*****************/
Delimiter $$
create procedure sp_ListarTipoEmpleado()
	begin
		select
			TipoEmpleado.codigoTipoEmpleado,
            TipoEmpleado.descripcion
				from TipoEmpleado;
	end $$
Delimiter ;

/*call sp_ListarTipoEmpleado();*/
				/****************ELIMINARTIPOEMPLEADO*****************/
Delimiter $$
create procedure sp_EliminarTipoEmpleado (
in _codigoTipoEmpleado int
)
	begin
		delete from TipoEmpleado where codigoTipoEmpleado = _codigoTipoEmpleado;
	end $$
Delimiter ;

				/****************ELIMINARTIPOEMPLEADO*****************/
Delimiter $$
create procedure sp_BuscarTipoEmpleado (
in _codigoTipoEmpleado int
)
	begin
		select
			TipoEmpleado.codigoTipoEmpleado,
            TipoEmpleado.descripcion
            from tipoempleado where codigoTipoEmpleado = _codigoTipoEmpleado;
	end $$
Delimiter ;

/*call sp_BuscarTipoEmpleado(1);*/

/**************************************PROCEDIMIENTO_EMPLEADO************************************/
Delimiter $$
create procedure sp_AgregarEmpleados(
in codigoEmpleado int,
in numeroEmpleado int,
in apellidoEmpleado varchar(150),
in nombreEmpleado varchar(150),
in direccionEmpleado varchar(150),
in telefonoContacto varchar (150),
in gradoCocinero varchar (150),
in codigoTipoEmpleado int
)
	begin
		insert into Empleados(codigoEmpleado,numeroEmpleado,apellidoEmpleado,nombreEmpleado,direccionEmpleado,telefonoContacto,gradoCocinero,codigoTipoEmpleado)
			values (codigoEmpleado,numeroEmpleado,apellidoEmpleado,nombreEmpleado,direccionEmpleado,telefonoContacto,gradoCocinero,codigoTipoEmpleado);
	end $$
Delimiter ;

call sp_AgregarEmpleados(1,1,'Aguilar Solomo','Jeremi Saul','7av 23-18 villa nueva z.12','1222-3456','pinche',1);
call sp_AgregarEmpleados(2,2,'Ajuchan Mendez','Alexander Mario','1av 2-8 Mixco z.10','6092-3556','subchef',2);
call sp_AgregarEmpleados(3,3,'Rodriguez Paz','Justin Alan','5av 4-20 San Miguel Petapa z.14','0987-0099','chef',3);
call sp_AgregarEmpleados(4,5,'Carino Conquero','Jeffrey Alan','5av 4-20 Amatitlan ','2233-0099','pinche',3);
call sp_AgregarEmpleados(5,6,'Chachavac Ruiz','Anderson Sol','4av 4-20 Amatitlan ','0099-0099','pinche',4);

				/****************ACTUALIZAREMPLEADOS*****************/
Delimiter $$
create procedure sp_ActualizarEmpleados(
in _codigoEmpleado int,
in _numeroEmpleado int,
in _apellidoEmpleado varchar(150),
in _nombreEmpleado varchar(150),
in _direccionEmpleado varchar(150),
in _telefonoContacto varchar (150),
in _gradoCocinero varchar (150),
in _codigoTipoEmpleado int
)
	begin
		update Empleados set
			numeroEmpleado = _numeroEmpleado,
            apellidoEmpleado = _apellidoEmpleado,
            nombreEmpleado = _nombreEmpleado,
            direccionEmpleado = _direccionEmpleado,
            telefonoContacto = _telefonoContacto,
            gradoCocinero = _gradoCocinero,
            codigoTipoEmpleado = _codigoTipoEmpleado
			Where codigoEmpleado = _codigoEmpleado;
            
    end $$
Delimiter ;

/*call sp_ActualizarEmpleados(2,2,'Ajuchan Mendez','Alejandra Nataly','1av 2-8 Mixco z.10','6092-3556','subchef',2);*/

				/****************LISTAREMPLEADOS*****************/
Delimiter $$
create procedure sp_ListarEmpleados()
	begin
		select
			Empleados.codigoEmpleado,
            Empleados.numeroEmpleado,
            Empleados.apellidoEmpleado,
            Empleados.nombreEmpleado,
            Empleados.direccionEmpleado,
            Empleados.telefonoContacto,
            Empleados.gradoCocinero,
            Empleados.codigoTipoEmpleado
				from Empleados;
	end $$
Delimiter ;

/*call sp_ListarEmpleados();*/
				/****************ELIMINAREMPLEADOS*****************/
Delimiter $$
create procedure sp_EliminarEmpleados(
in _codigoEmpleado int
)
	begin
		delete from Empleados where codigoEmpleado = _codigoEmpleado;
	end $$
Delimiter ;

/*call sp_EliminarEmpleados(5);*/
				/****************ELIMINAREMPLEADOS*****************/
Delimiter $$
create procedure sp_BuscarEmpleados(
in _codigoEmpleado int
)
	begin
		select
			Empleados.codigoEmpleado,
            Empleados.numeroEmpleado,
            Empleados.apellidoEmpleado,
            Empleados.nombreEmpleado,
            Empleados.direccionEmpleado,
            Empleados.telefonoContacto,
            Empleados.gradoCocinero,
            Empleados.codigoTipoEmpleado
				from Empleados where codigoEmpleado = _codigoEmpleado;
	end $$
Delimiter ;
/*call sp_BuscarEmpleados(2);*/

/********************************PROCEDIMIENTO_SERVICIOS_HAS_EMPLEADOS*****************************/
Delimiter $$
create procedure sp_AgregarServicios_has_Empleados(
in codigoServicio int,
in codigoEmpleado int,
in fechaEvento date,
in horaEvento time,
in lugarEvento varchar(150)
)
	begin
		insert into Servicios_has_Empleados(codigoServicio,codigoEmpleado,fechaEvento,horaEvento,lugarEvento)
			values ( codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
    end $$
Delimiter ;

call sp_AgregarServicios_has_Empleados(1,1,'20-10,11','09:00','Salon Gerneral');
call sp_AgregarServicios_has_Empleados(2,2,'20-07,11','19:00','El Artartico');
call sp_AgregarServicios_has_Empleados(3,3,'20-06,11','10:00','Barcelo');
call sp_AgregarServicios_has_Empleados(4,3,'20-08,21','11:00','Atenas');

				/****************ACTUALIZAR*****************/
Delimiter $$
create procedure sp_ActualizarServicios_has_Empleados(
in _Numero int,
in _codigoServicio int,
in _codigoEmpleado int,
in _fechaEvento date,
in _horaEvento time,
in _lugarEvento varchar(150)
)
	begin
		update Servicios_has_Empleados set
				codigoServicio = _codigoServicio,
				codigoEmpleado = _codigoEmpleado,
                fechaEvento = _fechaEvento, 
                horaEvento = _horaEvento,
                lugarEvento = _lugarEvento
					 where Numero = _Numero ;
	end $$
Delimiter ;

/*call sp_ActualizarServicios_has_Empleados (1,1,2,'20-03-04','12:00','Salon Generall');*/

				/****************LISTAR*****************/
Delimiter  $$
create procedure sp_ListarServicios_has_Empleados()
	begin
		select 
			Servicios_has_Empleados.Numero,
			Servicios_has_Empleados.codigoServicio,
            Servicios_has_Empleados.codigoEmpleado,
            Servicios_has_Empleados.fechaEvento,
            Servicios_has_Empleados.horaEvento,
            Servicios_has_Empleados.lugarEvento
				from Servicios_has_Empleados; 
    end $$
Delimiter ;

				/***************ELIMINAR*****************/

Delimiter $$
create procedure sp_EliminarServicios_has_Empleados(
in _lugarEvento varchar(100)
)
	begin
		delete from Servicios_has_Empleados where lugarEvento = _lugarEvento;
    end $$
Delimiter ;

/*call sp_EliminarServicios_has_Empleados(4);*/

				/***************ELIMINAR*****************/
Delimiter $$
create procedure sp_BuscarServicios_has_Empleados(
in _codigoServicio int
)
	begin
		select 
			Servicios_has_Empleados.codigoServicio,
            Servicios_has_Empleados.codigoEmpleado,
            Servicios_has_Empleados.fechaEvento,
            Servicios_has_Empleados.horaEvento,
            Servicios_has_Empleados.lugarEvento
				from Servicios_has_Empleados where codigoServicio = _codigoServicio ;
	end $$
Delimiter ;
/****************************************************************************************************/
Delimiter $$
create procedure sp_ReportePresupuestos(in codEmpresa int)
	begin
		select * from Empresas E inner join Servicios S on E.codigoEmpresa = S.codigoEmpresa where E.codigoEmpresa = codEmpresa;
			end $$
Delimiter ;
/****************************************************************************************************/
Delimiter $$
create procedure sp_SubReportePresupuestos(in codEmpresa  int)
	begin
		select * from Empresas E inner join Presupuesto P on E.codigoEmpresa = P.codigoEmpresa where 
			E.codigoEmpresa = codEmpresa group by P.cantidadPresupuesto;
				end $$
Delimiter ;
/****************************************************************************************************/
Delimiter $$
create procedure sp_ReporteServicio (in codServicio int)
	begin
    
select  S.codigoServicio, S.fechaServicio, S.lugarServicio, S.telefonoContacto, S.tipoServicio, P.nombrePlato, P.descripcionPlato, P.precioPlato, P.cantidad, T.descripcion, u.nombreProducto from  servicios_has_platos SP
	inner join servicios S on SP.codigoServicio = S.codigoServicio
		inner join platos p on SP.codigoPlato = P.codigoPlato 
			inner join tipoplato T on P.codigoTipoPlato = T.codigoTipoPlato
				inner join productos U on P.codigoplato = U.codigoProducto
					where S.codigoServicio = codServicio ;
						end $$
Delimiter ;
/****************************************************************************************************/