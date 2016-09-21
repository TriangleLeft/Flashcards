# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Provides rules for creating a static library framework.
#
# The including makefile must define these variables:
#   STATIC_FRAMEWORK_NAME
#
# The including makefile may define these variables:
#   STATIC_FRAMEWORK_RESOURCE_FILES
#   STATIC_FRAMEWORK_HEADERS         (defaults to TRANSLATE_HEADERS)
#   STATIC_FRAMEWORK_PUBLIC_HEADERS  (defaults to STATIC_FRAMEWORK_HEADERS)
#   STATIC_LIBRARY_NAME              (defaults to FAT_LIB_NAME)
#   STATIC_HEADERS_DIR               (defaults to GEN_OBJC_DIR)
#
# This file defines the following to be used by the including file:
#   STATIC_FRAMEWORK_DIR
#   STATIC_FRAMEWORK_RESOURCES_DIR
#
# Author: Tom Ball
# Modified by: Aleksey Kurnosenko

BUILD_DIR = build/j2objcOutputs
STATIC_LIBRARY_NAME = core-j2objc
# Framework name
STATIC_FRAMEWORK_NAME = FlashcardsCore
# Destination dir
DIST_FRAMEWORK_DIR = build/

STATIC_FRAMEWORK_DIR = $(DIST_FRAMEWORK_DIR)/$(STATIC_FRAMEWORK_NAME).framework
STATIC_LIBRARY = $(BUILD_DIR)/lib/iosDebug/lib$(STATIC_LIBRARY_NAME).a
STATIC_HEADERS_DIR = $(BUILD_DIR)/src/main/objc

STATIC_FRAMEWORK_HEADERS := $(shell find $(STATIC_HEADERS_DIR) -name "*.h" | sed -e 's/[$$]/\\$$/g')
STATIC_FRAMEWORK_HEADERS := $(STATIC_FRAMEWORK_HEADERS:$(STATIC_HEADERS_DIR)/%=%)

STATIC_FRAMEWORK_RESOURCES_DIR = $(STATIC_FRAMEWORK_DIR)/Versions/A/Resources

FRAMEWORK_HEADER = $(BUILD_DIR)/src/main/objc/$(STATIC_FRAMEWORK_NAME).h
MODULE_MAP = $(BUILD_DIR)/module.modulemap

framework: $(STATIC_FRAMEWORK_DIR)
	@:


$(STATIC_FRAMEWORK_DIR): $(STATIC_LIBRARY) $(FRAMEWORK_HEADER) $(MODULE_MAP)
	@echo building $(STATIC_FRAMEWORK_NAME) framework
	@mkdir -p $(STATIC_FRAMEWORK_DIR)/Versions/A/Headers
	@/bin/ln -sfh A $(STATIC_FRAMEWORK_DIR)/Versions/Current
	@/bin/ln -sfh Versions/Current/Headers $(STATIC_FRAMEWORK_DIR)/Headers
	@/bin/ln -sfh Versions/Current/$(STATIC_FRAMEWORK_NAME) \
	    $(STATIC_FRAMEWORK_DIR)/$(STATIC_FRAMEWORK_NAME)
	@tar cf - -C $(STATIC_HEADERS_DIR) $(STATIC_FRAMEWORK_HEADERS) \
	    | tar xfp - -C $(STATIC_FRAMEWORK_DIR)/Versions/A/Headers
	@cp $(STATIC_LIBRARY) $(STATIC_FRAMEWORK_DIR)/Versions/A/$(STATIC_FRAMEWORK_NAME)
	@install -m 0644 $(FRAMEWORK_HEADER) $(STATIC_FRAMEWORK_DIR)/Versions/A/Headers
	@install -m 0644 $(MODULE_MAP) $(STATIC_FRAMEWORK_DIR)/Versions/A/Headers/
	@touch $@

# Creates a framework "master" header file that includes all the framework's header files.
# This header is then test-compiled with all allowed warnings to verify it can be included
# by other projects.
$(FRAMEWORK_HEADER):
	@echo "//\n// $(STATIC_FRAMEWORK_NAME).h\n//\n" > $@
	@for f in $(STATIC_FRAMEWORK_HEADERS); do \
	    echo '#include <'$${f}'>'; done >> $@

$(MODULE_MAP):
	@echo "module" $(STATIC_FRAMEWORK_NAME) "{" > $(MODULE_MAP)
	@echo "  umbrella header" '"'$(STATIC_FRAMEWORK_NAME).h'"' >> $(MODULE_MAP)
	@echo >> $(MODULE_MAP)
	@echo "  export *" >> $(MODULE_MAP)
	@echo "  module * { export * }" >> $(MODULE_MAP)
	@echo "}" >> $(MODULE_MAP)

clean:
	@echo cleaning $(STATIC_FRAMEWORK_NAME) framework
	@rm -rf $(STATIC_FRAMEWORK_DIR)
	@rm $(FRAMEWORK_HEADER)
