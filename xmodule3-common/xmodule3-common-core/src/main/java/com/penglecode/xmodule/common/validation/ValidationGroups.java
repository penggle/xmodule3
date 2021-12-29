package com.penglecode.xmodule.common.validation;

import javax.validation.groups.Default;

public class ValidationGroups {

	//验证所属操作组定义
	
	public interface Create extends Default {}
	
	public interface Update extends Default {}

	public interface Merge extends Default {}

	public interface Delete extends Default {}
	
	public interface Remove {}
	
	public interface Enable {}
	
	public interface Disable {}
	
	public interface UpdatePwd {}
	
	public interface ResetPwd {}

	public interface UpdateState {}
	
	public interface UpdateFlag {}
	
	public interface UpdateType {}
	
	public interface TurnOn {}
	
	public interface TurnOff {}
	
	public interface Online {}
	
	public interface Offline {}
	
	public interface Reset {}

}
