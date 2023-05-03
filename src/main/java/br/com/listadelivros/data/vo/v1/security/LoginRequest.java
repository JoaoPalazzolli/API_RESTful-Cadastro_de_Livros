package br.com.listadelivros.data.vo.v1.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;

}