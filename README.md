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
`bitbake-layers add-layer /path/to/meta-richyo-sunxi`, then rebuild the image.
Start with a single-slot card and check the HDMI login console. The kernel
initializes the display, so capture SPL and U-Boot using the serial console.
On the board, check `cat /sys/class/drm/card*-HDMI-A-*/status` and confirm
that the connector reports `connected` with the monitor attached.

## Ethernet

Orange Pi Zero 3 images built with this layer install the Allwinner DWMAC
driver, its dependencies and the Motorcomm YT8531 PHY driver. The image's
network manager remains responsible for DHCP. Check driver binding and carrier
in the kernel journal, then confirm an IPv4 lease with `ip -4 addr show`.

## U-Boot

The layer also selects upstream U-Boot 2026.10 for `orange-pi-zero3`.
The prior 2026.01 image reached U-Boot on the physical board and its last
reported UART line was `DRAM: 2 GiB (total 0 Bytes)`. The new pin includes
upstream fixes for non-power-of-two DRAM sizes and changes to early bank
setup and relocation. Armbian's reference image uses the same base tag.
Yocto retains its board defconfig, TF-A and boot script. A cold boot over
UART should reach the MMC probe and `Starting kernel`; until that test
passes, the specific cause of the old stop remains unconfirmed.
