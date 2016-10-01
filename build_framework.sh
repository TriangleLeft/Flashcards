./gradlew :core:clean :core:j2objcBuild
cd core && make && make -f assets.mk && cd ..