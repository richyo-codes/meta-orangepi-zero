# SPDX-License-Identifier: MIT
require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

# meta-sunxi's shared append broadens COMPATIBLE_MACHINE for its own recipes.
COMPATIBLE_MACHINE:forcevariable = "^orange-pi-zero3$"
SRCREV = "5508406582f6f7120dce0c4b3059819a41788db2"
SRC_URI = "git://source.denx.de/u-boot/u-boot.git;protocol=https;branch=main;tag=v${PV}"
DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native"

# The shared append still supplies boot.cmd and the TF-A/SPL integration.
# These backports target other boards, and the DT name fix is upstream.
SRC_URI:remove = " \
    file://0001-nanopi_neo_air_defconfig-Enable-eMMC-support.patch \
    file://0002-Added-nanopi-r1-board-support.patch \
    file://0003-sunxi-H6-Enable-Ethernet-on-Orange-Pi-One-Plus.patch \
    file://0004-OrangePi-3-LTS-support.patch \
    file://avoid_double_vendor_prefix.patch \
"

CVE_STATUS[CVE-2026-33243] = "cpe-incorrect: The U-Boot FIT issue is CVE-2026-46728."
CVE_STATUS[CVE-2026-46728] = "fixed-version: Fixed upstream in 2092322b31cc8b1f8c9e2e238d1043ae0637b241, included in v2026.10."
