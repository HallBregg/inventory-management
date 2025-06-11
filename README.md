--mode ''
--mode production

-Dnpm.build.mode=production

docker build -t <> --platform linux/amd64,linux/arm64 --build-arg BUILD_MODE=production .