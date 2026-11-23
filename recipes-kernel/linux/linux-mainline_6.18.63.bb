# SPDX-License-Identifier: MIT
require recipes-kernel/linux/linux-mainline.inc

DESCRIPTION = "Linux LTS with the Armbian HDMI backport for Orange Pi Zero 3"
COMPATIBLE_MACHINE = "^orange-pi-zero3$"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
SRC_URI[sha256sum] = "81764a545072d160748beff4d9de2e7d95aa9f16e8261110288f067a2cc61d71"
SRC_URI:append:orange-pi-zero3 = " file://orange-pi-zero3-ethernet.cfg"

# The BSP's defconfig and base patches remain in the upstream layer.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
FILESEXTRAPATHS:append := ":${RICHYO_SUNXI_BSPDIR}/recipes-kernel/linux/linux-mainline:${RICHYO_SUNXI_BSPDIR}/recipes-kernel/linux/files"
require orange-pi-zero3-hdmi.inc

# Preserve Armbian's signed-off patch files byte-for-byte. The commit URL
# and checksums above record provenance for this third-party patch series.
ERROR_QA:remove = "patch-status"
