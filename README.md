# Orange Pi Zero 3 HDMI

This board layer extends `meta-sunxi`. It selects Linux 6.18.63 for
`orange-pi-zero3` and enables the DRM display pipeline, HDMI PHY and console.
The Orange Pi profiles in the setup manifests include the layer.

The HDMI patch series comes from [Armbian 99bc32375](https://github.com/armbian/build/tree/99bc323758a9448dc1a5274d6aaaca32769fef76/patch/kernel/archive/sunxi-6.18/patches.drm),
the revision recorded in the Orange Pi Zero 3 reference image. The recipe
uses patches 0001–0042 in Armbian's order and checks each SHA-256. Those
patches implement the DE33 mixer and planes, timing controllers, HDMI PHY and
board DT wiring. The patches are fetched unchanged, so this recipe treats
missing Yocto `Upstream-Status` headers as advisory. The 6.18.40 kernel shipped
by our `meta-sunxi` pin predates
the scaler change that patch 0019 expects. Use the matching 6.18.63 base when
testing this patch set. Recheck it on each Linux version bump and drop the
backport once a supported kernel provides the complete pipeline upstream.

For an existing Orange Pi build, add this directory with
`bitbake-layers add-layer /path/to/meta-orangepi-zero`, then rebuild the image.
Start with a single-slot card and check the HDMI login console. The kernel
initializes the display, so capture SPL and U-Boot using the serial console.
On the board, check `cat /sys/class/drm/card*-HDMI-A-*/status` and confirm
that the connector reports `connected` with the monitor attached.

## Ethernet

Orange Pi Zero 3 images built with this layer install the Allwinner DWMAC
driver, its dependencies and the Motorcomm YT8531 PHY driver. The image's
network manager remains responsible for DHCP. Check driver binding and carrier
in the kernel journal, then confirm an IPv4 lease with `ip -4 addr show`.

## Onboard SPI flash

The built-in SPI0 driver at `5010000.spi` requests DMA channels from the
modular Allwinner DMA driver. Include `kernel-module-sun6i-dma` in the image
so the kernel can bind SPI0 and discover the onboard SPI NOR. The SD card
boots through a separate MMC controller.

## Onboard Bluetooth

The Zero 3 kernel enables the UWE5622 SDIO host, the board's radio power
sequence, and the pinned [Armbian UWE5622 driver](https://github.com/armbian/uwe5622/tree/ef4ac088ae4cfb468843199e64d57589aacf22d0).
Install `uwe5622-bluetooth` to attach `ttyBT0` with BlueZ `btattach`. The
recipe fetches the radio calibration files at build time. The Linux patches
retain their upstream licensing. Before distributing a built image, check the
separate redistribution terms for radio firmware and calibration files.

On the board, `bluetoothctl list` should show the controller. If it is absent,
check the kernel log for SDIO detection and `journalctl -b -u
uwe5622-bluetooth.service` for the attach result.

The Zero 3 kernel configuration disables `ARCH_ROCKCHIP`. With both
architectures enabled, UWE5622 revision `ef4ac088` selects both sets of build
flags. Rockchip's `CONFIG_WCN_PARSE_DTS` prevents registration of the
Allwinner platform device. On Linux 6.18.63, loading `sprdbt_tty` during boot
then hits a NULL dereference in `get_loopcheck_status` through `stop_marlin`
and `rfkill_register`. Boot validation should show `marlin_probe ok!` and
reach the serial login, followed by Bluetooth controller discovery.

## U-Boot

The layer also selects upstream U-Boot 2026.10 for `orange-pi-zero3`.
The prior 2026.01 image reached U-Boot on the physical board and its last
reported UART line was `DRAM: 2 GiB (total 0 Bytes)`. The new pin includes
upstream fixes for non-power-of-two DRAM sizes and changes to early bank
setup and relocation. Armbian's reference image uses the same base tag.
Yocto retains its board defconfig, TF-A and boot script. A cold boot over
UART should reach the MMC probe and `Starting kernel`; until that test
passes, the specific cause of the old stop remains unconfirmed.
