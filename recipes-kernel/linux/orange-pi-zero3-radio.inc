# SPDX-License-Identifier: MIT
SRC_URI:append:orange-pi-zero3 = " \
    git://github.com/armbian/uwe5622.git;protocol=https;branch=master;name=uwe5622;destsuffix=uwe5622 \
    https://raw.githubusercontent.com/armbian/build/99bc323758a9448dc1a5274d6aaaca32769fef76/patch/kernel/archive/sunxi-6.18/patches.armbian/drv-nvmem-sunxi-add-chipid-serial-helpers.patch;name=uwe5622-sid \
    file://uwe5622-sunxi-addr.patch \
    file://orange-pi-zero3-uwe5622.patch \
    file://orange-pi-zero3-radio.cfg \
"
SRCREV_uwe5622 = "ef4ac088ae4cfb468843199e64d57589aacf22d0"
SRCREV_FORMAT = "uwe5622"
SRC_URI[uwe5622-sid.sha256sum] = "e7ad23a9b5331d0f132a152f49db008c6cb95da30c33990b13ee54b2c1e88c5b"

do_patch:prepend:orange-pi-zero3() {
    driver="${UNPACKDIR}/uwe5622"
    dest="${S}/drivers/net/wireless/uwe5622"
    install -d "$dest"
    cp -R "$driver/Kconfig" "$driver/Makefile" "$driver/unisocwcn" \
        "$driver/unisocwifi" "$driver/tty-sdio" "$dest/"
}
