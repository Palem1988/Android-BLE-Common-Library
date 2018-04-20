package no.nordicsemi.android.ble.common.util;

import android.support.annotation.NonNull;

/**
 * CRC-16 class is a helper that calculates different types of CRC.
 * Testing is based on https://www.lammertbies.nl/comm/info/crc-calculation.html
 */
public final class CRC16 {

	private CRC16() {
		// empty private constructor
	}

	/**
	 * Calculates CRC CCITT (Kermit) over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0x0000 initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.kermit
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 CCITT (Kermit).
	 */
	public static int CCITT_Kermit(final @NonNull byte[] data, final int offset, final int length) {
		return CRC(0x1021, 0x0000, data, offset, length, true, true, 0x0000);
	}

	/**
	 * Calculates CRC CCITT-FALSE over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0xFFFF initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-ccitt-false
	 * See: http://srecord.sourceforge.net/crc16-ccitt.html
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 CCITT-FALSE.
	 */
	public static int CCITT_FALSE(final @NonNull byte[] data, final int offset, final int length) {
//		Other implementation of the same algorithm:
//		int crc = 0xFFFF;
//
//		for (int i = offset; i < offset + length && i < data.length; ++i) {
//			crc = (((crc & 0xFFFF) >> 8) | (crc << 8));
//			crc ^= data[i];
//			crc ^= (crc & 0xFF) >> 4;
//			crc ^= (crc << 8) << 4;
//			crc ^= ((crc & 0xFF) << 4) << 1;
//		}

		return CRC(0x1021, 0xFFFF, data, offset, length, false, false, 0x0000);
	}

	/**
	 * Calculates CRC MCRF4XX over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0xFFFF initial value.
	 * <p>
	 * This method is used in Bluetooth LE CGMS service E2E-CRC calculation.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-mcrf4xx<br>
	 * See: http://ww1.microchip.com/downloads/en/AppNotes/00752a.pdf<br>
	 * See: https://www.bluetooth.com/specifications/gatt -> CGMS (1.0.1)
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 MCRF4XX.
	 */
	public static int MCRF4XX(final @NonNull byte[] data, final int offset, final int length) {
		return CRC(0x1021, 0xFFFF, data, offset, length, true, true, 0x0000);
	}

	/**
	 * Calculates CRC AUG-CCITT over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0x1D0F initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-aug-ccitt
	 * See: http://srecord.sourceforge.net/crc16-ccitt.html
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 AUG-CCITT.
	 */
	public static int AUG_CCITT(final @NonNull byte[] data, final int offset, final int length) {
		return CRC(0x1021, 0x1D0F, data, offset, length, false, false, 0x0000);
	}

	/**
	 * Calculates CRC-16 ARC over given range of bytes from the block of data.
	 * It is using the 0x8005 polynomial and 0x0000 initial value.
	 * <p>
	 * Input data and output CRC are reversed.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-arc
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16.
	 */
	public static int ARC(final @NonNull byte[] data, final int offset, final int length) {
		return CRC(0x8005, 0x0000, data, offset, length, true, true, 0x0000);
	}

	/**
	 * Calculates CRC-16 MAXIM over given range of bytes from the block of data.
	 * It is using the 0x8005 polynomial and 0x0000 initial value and XORs output with 0xFFFF.
	 * <p>
	 * Input data and output CRC are reversed.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-maxim
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 MAXIM.
	 */
	public static int MAXIM(final @NonNull byte[] data, final int offset, final int length) {
		return CRC(0x8005, 0x0000, data, offset, length, true, true, 0xFFFF);
	}

	/**
	 * Calculates the CRC over given range of bytes from the block of data with given polynomial and initial value.
	 * This method may also reverse input bytes and reverse output CRC.
	 *
	 * See: http://www.zorc.breitbandkatze.de/crc.html
	 *
	 * @param poly   Polynomial used to calculate the CRC16.
	 * @param init   Initial value to feed the buffer.
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @param refin  True if the input data should be reversed.
	 * @param refout True if the output data should be reversed.
	 * @return CRC calculated with given parameters.
	 */
	public static int CRC(final int poly, final int init, final @NonNull byte[] data, final int offset, final int length, final boolean refin, final boolean refout, final int xorout) {
		int crc = init;

		for (int i = offset; i < offset + length && i < data.length; ++i) {
			final byte b = data[i];
			for (int j = 0; j < 8; j++) {
				final int k = refin ? 7 - j : j;
				final boolean bit = ((b >> (7 - k) & 1) == 1);
				final boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit) crc ^= poly;
			}
		}

		if (refout) {
			return (Integer.reverse(crc) >>> 16) ^ xorout;
		} else {
			return (crc ^ xorout) & 0xFFFF;
		}
	}
}