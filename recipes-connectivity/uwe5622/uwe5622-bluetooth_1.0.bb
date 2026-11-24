# SPDX-License-Identifier: MIT
SUMMARY = "Attach the Orange Pi Zero 3 onboard Bluetooth radio to BlueZ"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "^orange-pi-zero3$"
SRC_URI = "file://uwe5622-bluetooth.service file://70-uwe5622-bluetooth.rules"
S = "${UNPACKDIR}"

inherit systemd
SYSTEMD_SERVICE:${PN} = "uwe5622-bluetooth.service"
SYSTEMD_AUTO_ENABLE = "disable"

RDEPENDS:${PN} = "bluez5 util-linux-rfkill kernel-module-hci-uart kernel-module-sprdbt-tty uwe5622-firmware uwe5622-bt-config"

do_install() {
    install -Dm 0644 "${S}/uwe5622-bluetooth.service" \
        "${D}${systemd_system_unitdir}/uwe5622-bluetooth.service"
    sed -i -e 's|@BINDIR@|${bindir}|g' -e 's|@SBINDIR@|${sbindir}|g' \
        "${D}${systemd_system_unitdir}/uwe5622-bluetooth.service"
    install -Dm 0644 "${S}/70-uwe5622-bluetooth.rules" \
        "${D}${nonarch_base_libdir}/udev/rules.d/70-uwe5622-bluetooth.rules"
}

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d/70-uwe5622-bluetooth.rules"
