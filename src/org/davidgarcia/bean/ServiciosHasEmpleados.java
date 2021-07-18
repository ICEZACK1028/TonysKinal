package org.davidgarcia.bean;

import java.util.Date;

public class ServiciosHasEmpleados {
    private int codigoServicios_has_Empleado;
    private int Servicios_codigoServicio;
    private int Empleado_codigoEmpleado;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;

    public ServiciosHasEmpleados(){
    
    }

    public ServiciosHasEmpleados(int codigoServicios_has_Empleado, int Servicios_codigoServicio, int Empleado_codigoEmpleado, Date fechaEvento, String horaEvento, String lugarEvento) {
        this.codigoServicios_has_Empleado = codigoServicios_has_Empleado;
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.Empleado_codigoEmpleado = Empleado_codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getCodigoServicios_has_Empleado(){
        return codigoServicios_has_Empleado;
    }
    public void setCodigoServicios_has_Empleado(int codigoServicios_has_Empleado){
        this.codigoServicios_has_Empleado = codigoServicios_has_Empleado;
    }
    public int getServicios_codigoServicio(){
        return Servicios_codigoServicio;
    }
    public void setServicios_codigoServicio(int Servicios_codigoServicio){
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }
    public int getEmpleado_codigoEmpleado(){
        return Empleado_codigoEmpleado;
    }
    public void setEmpleado_codigoEmpleado(int Empleado_codigoEmpleado){
        this.Empleado_codigoEmpleado = Empleado_codigoEmpleado;
    }
    public Date getFechaEvento(){
        return fechaEvento;
    }
    public void setFechaEvento(Date fechaEvento){
        this.fechaEvento = fechaEvento;
    }
    public String getHoraEvento(){
        return horaEvento;
    }
    public void setHoraEvento(String horaEvento){
        this.horaEvento = horaEvento;
    }
    public String getLugarEvento(){
        return lugarEvento;
    }
    public void setLugarEvento(String lugarEvento){
        this.lugarEvento = lugarEvento;
    }
}
