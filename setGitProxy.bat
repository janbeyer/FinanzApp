@echo off
rem set the proxy settings
set https_proxy=komserver:3128
set http_proxy=komserver:3128

git --version

echo https proxy is set to: komserver:3128
echo http  proxy is set to: komserver:3128

rem example
rem git clone https://github.com/StackTipsLab/Android-IntentService-Example.git