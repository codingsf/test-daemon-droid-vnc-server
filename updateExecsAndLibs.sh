#!/bin/bash

cd $(dirname $0)

for i in libs/*; do
  if [[ -d $i && -e $i/androidvncserver ]];then
    echo Moving $i/androidvncserver to $i/libandroidvncserver.so;
    mv $i/androidvncserver $i/libandroidvncserver.so;
  fi
  if [[ -d $i && -e $i/screenshot ]];then
  	# statically change name screenshot to libscreenshot.so so that in /data/.../lib has the file
	echo Moving $i/screenshot to $i/libscreenshot.so;
    mv $i/screenshot $i/libscreenshot.so;
  fi
done
cp -frv nativeMethods/libs/* libs
echo Done.
