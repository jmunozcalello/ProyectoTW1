package com.tallerwebi.dominio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String password;
  private String rol;
  private Boolean activo = false;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Ambiente> ambientes = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public void activar() {
    activo = true;
  }

  public List<Ambiente> getAmbientes() {
    return ambientes;
  }

  public void agregarAmbiente(Ambiente ambiente) {
    ambiente.setUsuario(this);
    this.ambientes.add(ambiente);
  }
}
