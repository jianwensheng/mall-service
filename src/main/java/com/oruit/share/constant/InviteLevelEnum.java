package com.oruit.share.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum InviteLevelEnum {
	/** 初级推广达人 */
	JUNIOR((short) 1, "初级推广达人", 0.02D, 0, 10),

	/** 中级推广达人 */
	MIDDLE((short) 2, "中级推广达人", 0.06D, 10, 50),

	/** 高级推广达人 */
	SENIOR((short) 3, "高级推广达人", 0.10D, 50, 5000),

	/** 城市合伙人 */
	CITY((short) 4, "城市合伙人", 0.00D, 5000, 10000);

	private short code;

	private String name;

	private double rate;

	private int minNumber;

	private int maxNumber;

	private InviteLevelEnum() {
	}

	private InviteLevelEnum(short code, String name, double rate, int minNumber, int maxNumber) {
		this.code = code;
		this.name = name;
		this.rate = rate;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
	}

	public short getCode() {
		return code;
	}

	public void setCode(short code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(int minNumber) {
		this.minNumber = minNumber;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public static InviteLevelEnum getInstance(short code) {
		List<InviteLevelEnum> list = Arrays.asList(InviteLevelEnum.values()).stream().filter((InviteLevelEnum type) -> {
			return type.code == code;
		}).collect(Collectors.toList());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
