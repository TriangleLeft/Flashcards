IMAGE_NAME = IMAGE_NAME

define ROOT_CONTENTS_BODY
{
  "info": {
    "version": 1,
    "author": "xcode"
  }
}
endef
export ROOT_CONTENTS_BODY

define IMAGE_CONTENTS_BODY
{
  "images": [
    {
      "idiom": "universal",
      "filename": "$(IMAGE_NAME).png",
      "scale": "1x"
    },
    {
      "idiom": "universal",
      "filename": "$(IMAGE_NAME)-1.png",
      "scale": "2x"
    },
    {
      "idiom": "universal",
      "filename": "$(IMAGE_NAME)-2.png",
      "scale": "3x"
    }
  ],
  "info": {
    "version": 1,
    "author": "xcode"
  }
}
endef
export IMAGE_CONTENTS_BODY

IMAGES_DIR = ../app/build/generated/res/pngs/debug
IMAGES_DIR1X = $(IMAGES_DIR)/drawable-mdpi
IMAGES_DIR2X = $(IMAGES_DIR)/drawable-xhdpi
IMAGES_DIR3X = $(IMAGES_DIR)/drawable-xxhdpi

BUNDLE_NAME = CoreAssets
DIST_FRAMEWORK_DIR = build

ASSETS_DIR = $(DIST_FRAMEWORK_DIR)/$(BUNDLE_NAME).xcassets
CONTENTS = Contents.json

# use mdpi folder as base
# find all images there
SOURCE_ASSETS := $(shell find $(IMAGES_DIR1X) -name "*.png")
# remove absolute path
SOURCE_ASSETS := $(SOURCE_ASSETS:$(IMAGES_DIR1X)/%=%)

assets: $(ASSETS_DIR)
	@:

$(ASSETS_DIR): $(SOURCE_ASSETS)
	@mkdir -p $(ASSETS_DIR)
	@echo "$$ROOT_CONTENTS_BODY" > $(ASSETS_DIR)/$(CONTENTS)

# For each png, create folder with same name, copy images from required dpi's
# to this folder, rename them, create contents.json for that image
%.png:
	$(eval TARGET := $(@:%.png=%))
	$(eval TARGET_DIR := $(TARGET:%=%.imageset))
	@echo Prepairing $(TARGET)
	@mkdir -p $(ASSETS_DIR)/$(TARGET_DIR)
	@cp $(IMAGES_DIR1X)/$@ $(ASSETS_DIR)/$(TARGET_DIR)/$(TARGET).png
	@cp $(IMAGES_DIR2X)/$@ $(ASSETS_DIR)/$(TARGET_DIR)/$(TARGET)-1.png
	@cp $(IMAGES_DIR3X)/$@ $(ASSETS_DIR)/$(TARGET_DIR)/$(TARGET)-2.png
	@echo "$$IMAGE_CONTENTS_BODY" | sed 's/$(IMAGE_NAME)/$(TARGET)/g' > $(ASSETS_DIR)/$(TARGET_DIR)/$(CONTENTS)

clean:
	@echo cleaning $(ASSETS_DIR)
	@rm -rf $(ASSETS_DIR)
