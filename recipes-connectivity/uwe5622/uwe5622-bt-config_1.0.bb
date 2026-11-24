# SPDX-License-Identifier: MIT
SUMMARY = "Bluetooth calibration settings for the UWE5622 radio"
LICENSE = "CLOSED"
COMPATIBLE_MACHINE = "^orange-pi-zero3$"

FIRMWARE_REV = "db8cc456dad5ad511b04f8a5130acda392cad20b"
FIRMWARE_URL = "https://raw.githubusercontent.com/armbian/firmware/${FIRMWARE_REV}"
SRC_URI = " \
    ${FIRMWARE_URL}/bt_configure_pskey.ini;name=pskey;downloadfilename=uwe5622-${FIRMWARE_REV}-pskey.ini \
    ${FIRMWARE_URL}/bt_configure_rf.ini;name=rf;downloadfilename=uwe5622-${FIRMWARE_REV}-rf.ini \
"
SRC_URI[pskey.sha256sum] = "dea8104861a7ad104b3d6686e073f821cb0250b1b9d52fcc2ef7e69e8249b5dc"
SRC_URI[rf.sha256sum] = "4044252baefdfa0e884b61d4c42f4d07f74a8f3121ba1923e782968d013d617d"
S = "${UNPACKDIR}"

do_install() {
    install -Dm 0644 "${S}/uwe5622-${FIRMWARE_REV}-pskey.ini" \
        "${D}${nonarch_base_libdir}/firmware/uwe5622/bt_configure_pskey.ini"
    install -Dm 0644 "${S}/uwe5622-${FIRMWARE_REV}-rf.ini" \
        "${D}${nonarch_base_libdir}/firmware/uwe5622/bt_configure_rf.ini"
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/uwe5622"
