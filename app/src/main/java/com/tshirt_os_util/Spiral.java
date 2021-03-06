package com.tshirt_os_util;

public class Spiral {

	public byte[][] btn1Img1 = new byte[][] {

			{ 0x00, 0x00, (byte) 0x3E, (byte) 0xF0, (byte) 0x0B, (byte) 0x0B,
					(byte) 0xC0, (byte) 0x02, (byte) 0x2C, 0x00, (byte) 0x3F,
					0x00, 0x00, (byte) 0x3F, (byte) 0xC0, (byte) 0x0B,
					(byte) 0x2F, 0x00, (byte) 0x0A, 0x00, 0x00, (byte) 0x3C,
					(byte) 0x80, (byte) 0x0F, (byte) 0x0F, (byte) 0x0E, 0x00,
					(byte) 0xBE, 0x00, 0x00, 0x00, (byte) 0x02, (byte) 0x80,
					(byte) 0x0F, 0x00, (byte) 0xBF, (byte) 0xF0, (byte) 0x02,
					(byte) 0x2C, 0x00, (byte) 0xC0, (byte) 0x0B, (byte) 0x0E,
					0x00, (byte) 0xE0, (byte) 0x0B, (byte) 0x3C, (byte) 0xA0,
					0x00, (byte) 0x80, (byte) 0x0F, 0x00, 0x00, (byte) 0x03,
					(byte) 0xB0, (byte) 0xE0, 0x00, (byte) 0xC0, (byte) 0x03,
					(byte) 0x08, 0x00, 0x00, 0x00, (byte) 0xC0, (byte) 0x28,
					0x00, (byte) 0x80, (byte) 0x2F, (byte) 0x3C, (byte) 0xC0,
					(byte) 0x02, (byte) 0xC0, (byte) 0x3C, 0x00, (byte) 0x80,
					(byte) 0x0B, (byte) 0xF8, (byte) 0xC0, (byte) 0x03,
					(byte) 0x02, (byte) 0x2E, (byte) 0x2C, 0x00, 0x00,
					(byte) 0xE0, (byte) 0x80, (byte) 0x83, (byte) 0x0B,
					(byte) 0x0E, (byte) 0x2E, (byte) 0x08, 0x00, 0x00, 0x00,
					(byte) 0x02, (byte) 0x0F, 0x00, (byte) 0x0F, (byte) 0x0F,
					(byte) 0x0A, (byte) 0x0B, 0x00, 0x00, (byte) 0x0B, 0x00,
					(byte) 0x0A, (byte) 0x0F, (byte) 0x0B, (byte) 0x0E,
					(byte) 0x0F, 0x00, 0x00, (byte) 0x0A, 0x00, (byte) 0x0B,
					(byte) 0x0B, (byte) 0x08, (byte) 0x0F, (byte) 0x0B, 0x00,
					(byte) 0x0F, 0x00, 0x00, (byte) 0x02, (byte) 0x80,
					(byte) 0x0B, (byte) 0x0F, (byte) 0x02, (byte) 0x0F,
					(byte) 0x0E, 0x00, 0x00, 0x00, (byte) 0x83, (byte) 0x83,
					(byte) 0x0B, (byte) 0x0F, (byte) 0x0F, (byte) 0x0C,
					(byte) 0xE0, (byte) 0x0A, (byte) 0x80, (byte) 0xC3,
					(byte) 0x03, 0x00, (byte) 0x2E, (byte) 0x3C, (byte) 0xF0,
					(byte) 0x0F, 0x00, (byte) 0xC0, (byte) 0xC3, 0x00,
					(byte) 0x0C, (byte) 0xB8, 0x00, 0x00, (byte) 0x08, 0x00,
					(byte) 0xC0, (byte) 0x28, 0x00, (byte) 0x20, 0x00, 0x00,
					(byte) 0x3F, 0x00, (byte) 0xC0, (byte) 0xBC, (byte) 0x80,
					0x00, (byte) 0xF8, (byte) 0x82, (byte) 0x0F, (byte) 0x2C,
					0x00, (byte) 0xF0, (byte) 0xC0, (byte) 0x03, (byte) 0xF8,
					(byte) 0x03, (byte) 0x02, (byte) 0x2F, 0x00, (byte) 0xE0,
					(byte) 0x80, (byte) 0x0F, 0x00, 0x00, (byte) 0x80,
					(byte) 0x0F, (byte) 0x0E, (byte) 0x02, 0x00, (byte) 0x0A,
					0x00, 0x00, 0x00, (byte) 0x02, (byte) 0x0F, (byte) 0x03,
					(byte) 0x28, 0x00, (byte) 0x3F, (byte) 0xE0, (byte) 0x0B,
					(byte) 0xC0, (byte) 0x0B, (byte) 0x0B, (byte) 0xBC, 0x00,
					(byte) 0xBF, (byte) 0xF0, (byte) 0x03, 0x00, (byte) 0x82,
					(byte) 0x02, (byte) 0xF8, 0x00, 0x00, 0x00, 0x00,
					(byte) 0x0B, 0x00, },
			{ (byte) 0xF0, 0x00, (byte) 0xF8, (byte) 0x83, (byte) 0x3F, 0x00,
					(byte) 0x08, (byte) 0xC0, (byte) 0xBC, 0x00, (byte) 0xB8,
					0x00, 0x00, (byte) 0xBC, 0x00, (byte) 0x02, (byte) 0x2C,
					(byte) 0x38, 0x00, 0x00, 0x00, (byte) 0xF8, (byte) 0x02,
					(byte) 0x0F, 0x00, (byte) 0x3E, 0x00, (byte) 0xF0,
					(byte) 0x03, (byte) 0xE0, 0x00, (byte) 0x3E, 0x00,
					(byte) 0x2E, (byte) 0x20, (byte) 0xF0, (byte) 0x03, 0x00,
					0x00, (byte) 0x28, 0x00, 0x00, (byte) 0xBE, 0x00,
					(byte) 0x80, (byte) 0x0F, (byte) 0x30, 0x00, 0x00, 0x00,
					(byte) 0x2F, 0x00, 0x00, (byte) 0x3F, (byte) 0xF0, 0x00,
					(byte) 0xB0, 0x00, 0x00, (byte) 0xF8, 0x00, (byte) 0x28,
					(byte) 0xF0, (byte) 0xC2, (byte) 0x3C, (byte) 0x20, 0x00,
					(byte) 0xFC, 0x00, 0x00, (byte) 0x80, (byte) 0xC0,
					(byte) 0x3C, (byte) 0x3C, 0x00, 0x00, (byte) 0xE0, 0x00,
					(byte) 0x02, (byte) 0x80, 0x00, (byte) 0x3C, (byte) 0x3C,
					0x00, (byte) 0xE0, (byte) 0x83, (byte) 0x0B, 0x00, 0x00,
					(byte) 0x0E, (byte) 0x3E, (byte) 0x30, (byte) 0x80,
					(byte) 0x83, (byte) 0x0B, (byte) 0x0B, 0x00, 0x00,
					(byte) 0x0E, (byte) 0x3E, 0x00, 0x00, (byte) 0x0F,
					(byte) 0x0F, (byte) 0x0F, 0x00, 0x00, (byte) 0x0A, 0x00,
					0x00, 0x00, (byte) 0x0F, (byte) 0x0F, (byte) 0x02, 0x00,
					0x00, 0x00, 0x00, 0x00, (byte) 0x0A, (byte) 0x0F,
					(byte) 0x0F, 0x00, 0x00, (byte) 0x80, (byte) 0x0B, 0x00,
					0x00, (byte) 0x02, (byte) 0x0F, (byte) 0x0F, (byte) 0x28,
					(byte) 0xC0, (byte) 0x83, (byte) 0x03, 0x00, 0x00,
					(byte) 0x0F, (byte) 0x2F, (byte) 0xFC, (byte) 0xE0,
					(byte) 0xC2, (byte) 0x83, (byte) 0x02, 0x00, 0x00,
					(byte) 0x2C, (byte) 0xF0, 0x00, (byte) 0xE0, (byte) 0xC2,
					(byte) 0x03, (byte) 0x2C, 0x00, 0x00, 0x00, 0x00, 0x00,
					(byte) 0xE0, (byte) 0xC2, (byte) 0x3C, (byte) 0xB0, 0x00,
					(byte) 0x02, 0x00, (byte) 0x02, (byte) 0xE0, (byte) 0xE0,
					(byte) 0x3C, (byte) 0xF0, 0x00, (byte) 0x3F, (byte) 0xF0,
					(byte) 0x03, 0x00, (byte) 0xF0, (byte) 0x20, (byte) 0xE0,
					(byte) 0x03, (byte) 0x3E, (byte) 0xF0, (byte) 0x02,
					(byte) 0x02, (byte) 0xA0, 0x00, (byte) 0x80, 0x00, 0x00,
					0x00, (byte) 0xC0, (byte) 0x0B, 0x00, (byte) 0x02,
					(byte) 0x0B, (byte) 0xE0, 0x00, 0x00, (byte) 0xF0,
					(byte) 0x02, (byte) 0x02, (byte) 0x02, (byte) 0x2F,
					(byte) 0xE0, (byte) 0x0F, (byte) 0xB8, 0x00, (byte) 0xC0,
					(byte) 0x03, 0x00, (byte) 0x3E, (byte) 0x80, (byte) 0x0B,
					(byte) 0xFE, 0x00, (byte) 0xF0, (byte) 0x02, (byte) 0x20,
					(byte) 0x28, 0x00, 0x00, (byte) 0x08, 0x00, (byte) 0xA0,
					0x00, },
			{ (byte) 0xF0, (byte) 0x03, (byte) 0xC0, (byte) 0x2F, (byte) 0xFC,
					(byte) 0x02, (byte) 0xF8, (byte) 0x80, (byte) 0xB0,
					(byte) 0xC0, (byte) 0x02, 0x00, (byte) 0xE0, 0x00, 0x00,
					0x00, 0x00, (byte) 0xF0, (byte) 0x03, 0x00, 0x00,
					(byte) 0xE0, (byte) 0x02, 0x00, (byte) 0x02, (byte) 0xB8,
					0x00, (byte) 0x80, (byte) 0x0B, (byte) 0xC0, (byte) 0x0B,
					(byte) 0x3C, (byte) 0x03, 0x00, (byte) 0xF0, (byte) 0x83,
					(byte) 0x3F, 0x00, (byte) 0x0B, (byte) 0xBC, (byte) 0x82,
					0x00, (byte) 0xF8, (byte) 0x02, (byte) 0x28, 0x00, 0x00,
					(byte) 0xB0, (byte) 0xF0, (byte) 0x02, 0x00, 0x00, 0x00,
					(byte) 0x3C, (byte) 0x80, 0x00, (byte) 0xF0, (byte) 0xE0,
					0x00, (byte) 0xE0, (byte) 0x0B, (byte) 0xFC, (byte) 0xE0,
					(byte) 0x02, (byte) 0x30, (byte) 0xF0, 0x00, (byte) 0xE0,
					(byte) 0x0F, (byte) 0xF0, (byte) 0xC0, (byte) 0x03, 0x00,
					(byte) 0x3C, (byte) 0xF0, 0x00, 0x00, 0x00, (byte) 0xC0,
					(byte) 0x83, 0x00, (byte) 0x20, (byte) 0xBC, 0x00,
					(byte) 0x80, (byte) 0x03, 0x00, (byte) 0x80, (byte) 0x0E,
					0x00, (byte) 0x38, (byte) 0xF0, (byte) 0x82, (byte) 0x0B,
					(byte) 0x0B, 0x00, (byte) 0x0F, 0x00, 0x00, (byte) 0xF8,
					(byte) 0x03, (byte) 0x0F, (byte) 0x0F, 0x00, (byte) 0x0F,
					(byte) 0x0F, 0x00, 0x00, 0x00, (byte) 0x0A, (byte) 0x0F,
					(byte) 0x0F, (byte) 0x0A, (byte) 0x0F, (byte) 0x0B, 0x00,
					0x00, 0x00, (byte) 0x0B, (byte) 0x0F, 0x00, (byte) 0x0F,
					(byte) 0x0F, (byte) 0x0B, 0x00, 0x00, 0x00, (byte) 0x0F,
					0x00, (byte) 0x02, (byte) 0x0F, (byte) 0x2F, (byte) 0xE0,
					(byte) 0x02, 0x00, (byte) 0x82, (byte) 0x0E, 0x00,
					(byte) 0x0A, (byte) 0x3C, (byte) 0xF8, (byte) 0x82,
					(byte) 0x02, (byte) 0x80, (byte) 0x0F, 0x00, 0x00, 0x00,
					(byte) 0x3C, (byte) 0xE0, (byte) 0x02, 0x00, (byte) 0x2E,
					(byte) 0x3C, (byte) 0x80, 0x00, 0x00, (byte) 0xF8,
					(byte) 0xE0, 0x00, (byte) 0x2C, (byte) 0xBC, (byte) 0xE0,
					(byte) 0x03, 0x00, (byte) 0x20, (byte) 0xF0, 0x00, 0x00,
					(byte) 0xF0, (byte) 0xC0, (byte) 0x0B, (byte) 0xBA, 0x00,
					(byte) 0xB8, (byte) 0xF0, 0x00, 0x00, 0x00, (byte) 0x02,
					(byte) 0xBF, 0x00, 0x00, (byte) 0xBC, (byte) 0xC0,
					(byte) 0x02, (byte) 0x28, 0x00, 0x00, (byte) 0xA0, 0x00,
					(byte) 0x3C, (byte) 0xC0, (byte) 0x0B, (byte) 0xFC, 0x00,
					0x00, (byte) 0xFC, 0x00, 0x00, 0x00, (byte) 0x0B,
					(byte) 0xF8, (byte) 0x80, (byte) 0x0A, (byte) 0x3C,
					(byte) 0x80, 0x00, (byte) 0x08, 0x00, 0x00, (byte) 0xC0,
					(byte) 0x2F, 0x00, (byte) 0xF8, 0x00, (byte) 0x2E,
					(byte) 0x80, (byte) 0x03, (byte) 0x80, (byte) 0x0A, 0x00,
					(byte) 0xBE, 0x00, },
			{ (byte) 0x80, (byte) 0x03, (byte) 0x0A, (byte) 0xFE, (byte) 0xE0,
					(byte) 0x0B, (byte) 0xF0, (byte) 0x02, 0x00, (byte) 0xC0,
					(byte) 0x0F, 0x00, (byte) 0xC0, (byte) 0x0F, (byte) 0xC0,
					(byte) 0x03, (byte) 0x0F, (byte) 0xC0, (byte) 0x03, 0x00,
					0x00, 0x00, 0x00, (byte) 0x02, (byte) 0x0F, 0x00, 0x00,
					(byte) 0x0E, (byte) 0x28, 0x00, (byte) 0x0B, 0x00,
					(byte) 0x83, (byte) 0x03, (byte) 0xC0, (byte) 0x2F,
					(byte) 0xFC, (byte) 0x02, (byte) 0x2F, (byte) 0xB0,
					(byte) 0xC0, (byte) 0x03, (byte) 0x80, (byte) 0x02,
					(byte) 0xF0, (byte) 0x02, (byte) 0x3C, (byte) 0xF0,
					(byte) 0xE0, (byte) 0x82, (byte) 0x03, 0x00, 0x00, 0x00,
					0x00, (byte) 0xF0, (byte) 0x80, (byte) 0xE0, (byte) 0x03,
					0x00, (byte) 0x0A, (byte) 0xF0, 0x00, (byte) 0x80, 0x00,
					(byte) 0xF0, (byte) 0xC0, (byte) 0x0B, (byte) 0x3F,
					(byte) 0xF0, (byte) 0x82, (byte) 0x03, (byte) 0x08, 0x00,
					(byte) 0xF0, (byte) 0x03, (byte) 0x3C, (byte) 0xC0,
					(byte) 0xC3, (byte) 0x03, (byte) 0x2E, 0x00, (byte) 0xA0,
					0x00, 0x00, 0x00, (byte) 0x80, (byte) 0x0B, (byte) 0x0F,
					(byte) 0x0A, 0x00, (byte) 0x80, (byte) 0x02, 0x00, 0x00,
					(byte) 0x03, (byte) 0x0F, (byte) 0x0F, (byte) 0x0A,
					(byte) 0x80, (byte) 0x0B, (byte) 0x0F, 0x00, 0x00, 0x00,
					(byte) 0x0F, (byte) 0x0F, (byte) 0x02, (byte) 0x0E,
					(byte) 0x0F, (byte) 0x0F, 0x00, 0x00, (byte) 0x0B,
					(byte) 0x0F, (byte) 0x0B, (byte) 0x08, (byte) 0x0F,
					(byte) 0x0F, (byte) 0x0A, 0x00, 0x00, (byte) 0x0F,
					(byte) 0x0B, 0x00, (byte) 0x02, (byte) 0x0B, (byte) 0x0F,
					(byte) 0x0F, 0x00, 0x00, (byte) 0x0B, 0x00, 0x00,
					(byte) 0x82, (byte) 0x0B, (byte) 0x0F, (byte) 0x0A, 0x00,
					0x00, (byte) 0x3E, 0x00, (byte) 0x80, (byte) 0x83,
					(byte) 0x0F, (byte) 0x2E, (byte) 0x28, (byte) 0x80,
					(byte) 0x3F, (byte) 0x20, 0x00, (byte) 0xC0, 0x00,
					(byte) 0x3C, (byte) 0xF8, 0x00, 0x00, (byte) 0xB8, 0x00,
					(byte) 0xC0, 0x00, (byte) 0x28, (byte) 0xF0, (byte) 0x02,
					0x00, (byte) 0x3E, (byte) 0xB0, 0x00, (byte) 0x20, 0x00,
					(byte) 0x80, (byte) 0x80, (byte) 0x0A, (byte) 0x0E,
					(byte) 0xBC, 0x00, (byte) 0xF0, 0x00, (byte) 0x02,
					(byte) 0xE0, (byte) 0x0F, 0x00, (byte) 0x2E, (byte) 0x38,
					(byte) 0xE0, (byte) 0x02, (byte) 0x0F, 0x00, (byte) 0x02,
					0x00, (byte) 0x08, (byte) 0x3E, (byte) 0xC0, (byte) 0x02,
					(byte) 0x3E, 0x00, 0x00, (byte) 0x2C, 0x00, (byte) 0x0F,
					(byte) 0x03, 0x00, (byte) 0x28, (byte) 0xB8, (byte) 0x80,
					(byte) 0x3F, 0x00, (byte) 0x02, (byte) 0x0F, (byte) 0xB0,
					0x00, (byte) 0xFC, (byte) 0x83, (byte) 0x0B, (byte) 0x08,
					(byte) 0x80, (byte) 0x2F, (byte) 0xF0, (byte) 0x03,
					(byte) 0x80, (byte) 0x02, 0x00, (byte) 0x3F, 0x00, }, };

}
