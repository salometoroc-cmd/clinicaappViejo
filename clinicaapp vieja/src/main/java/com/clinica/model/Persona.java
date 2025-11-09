package com.clinica.model;

public abstract class Persona {
    protected String documento;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;

    // Constructor
    public Persona(String documento, String nombre, String apellido, String email, String telefono) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public String getDocumento() { 
        return documento; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public String getApellido() { 
        return apellido; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getTelefono() { 
        return telefono; 
    }

    public void setDocumento(String documento) { 
        this.documento = documento; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public abstract TipoPersona getTipoPersona();
}
