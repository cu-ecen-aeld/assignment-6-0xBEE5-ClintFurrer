# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit update-rc.d

# TODO: Set this  with the path to your assignments repo.  Use https protocol and a public
# repo, or see assignment instructions for use with ssh keys
SRC_URI = "https://github.com/cu-ecen-aeld/assignment-3-0xBEE5-ClintFurrer.git;protocol=https;nobranch=1"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = 91d1b8980ac84428f10321283b01233a23a6f7b5

# TODO: Add the aesdsocket application and any other files you will install in do_install below
# See https://github.com/openembedded/openembedded-core/blob/wrynose/meta/conf/bitbake.conf for path prefixes like ${bindir}
# or ${sysconfdir}"
FILES:${PN} += "${bindir}/aesdsocket \
				 ${bindir}/finder.sh \
				 ${bindir}/finder-test.sh \
				 ${base_bindir}/writer \
				 ${base_bindir}/* \
				 ${sysconfdir}/conf/*"
INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop"				 

# TODO: customize these as necessary for any libraries you need for your application
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile () {
    # TODO: switch to the server directory where your source code to be built is located
	(
		cd ${S}/server
		oe_runmake
	)
	(
		cd ${S}/finder-app
		oe_runmake
	)
}

#TODO: add initscript necessary changes here

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See examples at https://github.com/cu-ecen-aeld/yocto-hello-world for your relevant build as well
	# Remember to copy files relative to their location after building, which may be under the server subdirectory

	install -d ${D}${sysconfdir}/conf
	install -d ${D}${base_bindir}
	install -d ${D}${bindir}
	install -d ${D}${sysconfdir}/init.d

	install -m 0755 ${B}/conf/* ${D}${sysconfdir}/conf/
	install -m 0755 ${B}/assignment-autotest/test/assignment4/* ${D}${base_bindir}/

	install -m 0755 ${B}/server/aesdsocket ${D}${bindir}/
	install -m 0755 ${B}/server/aesdsocket-start-stop ${D}${sysconfdir}/init.d

	install -m 0755 ${B}/finder-app/writer ${D}${base_bindir}/
	install -m 0755 ${B}/finder-app/finder.sh ${D}${bindir}/
	install -m 0755 ${B}/finder-app/finder-test.sh ${D}${bindir}/
}
