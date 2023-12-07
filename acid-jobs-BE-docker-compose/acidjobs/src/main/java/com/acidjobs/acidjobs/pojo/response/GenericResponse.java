package com.acidjobs.acidjobs.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenericResponse<T> {
	private String status;
	private String message;
	private T data;
}
