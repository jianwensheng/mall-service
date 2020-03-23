#!/usr/bin/env bash

#PAC_HOME=/Users/wangyt/project/ruit/international_app_share/target
PAC_HOME=/usr/local/app-share-new


cd $PAC_HOME

ps -ef | grep -v grep | grep -i 'cashzine_share.jar\|/opt/open_acct2' | awk '{print $2}' | sed -e "s/^/kill -9 /g" | sh -

nohup java -jar $PAC_HOME/cashzine_share.jar  >./app_share.out 2>&1 &

tail -f $PAC_HOME/app_share.out
