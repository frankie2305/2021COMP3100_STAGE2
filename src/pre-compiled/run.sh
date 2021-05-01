#!/bin/bash
# make sure you have your client and ds-sim (ds-server and ds-client) all in the same directory and test configuration files in configs directory 
# to kill multiple runaway processes, use 'pkill runaway_process_name'
# For the Java implementation, use the following format: ./demoS1Final.sh [Java specific arugment...] [-n] your_client.class [your client specific argument...]
i=1

if [ $# -lt 2 ]; then
    echo "Usage: -c configDir"
    exit
fi

while getopts "c:" opt;
do
    case ${opt} in
        c)  configDir=$OPTARG;;
    esac
done

if [[ ! -d $configDir ]]; then
	echo "No $configDir found!"
	exit
fi

trap "kill 0" EXIT

for config in $configDir/*.xml; do
	echo "$config"
	sleep 2
	./ds-server -c $config -v all > ff$i-all.log&
	sleep 4
	./ds-client -a ff
	sleep 2
	./ds-server -c $config -v all > bf$i-all.log&
	sleep 4
	./ds-client -a bf
	sleep 2
	./ds-server -c $config -v all > wf$i-all.log&
	sleep 4
	./ds-client -a wf
	sleep 2
	./ds-server -c $config -v brief > ff$i-brief.log&
	sleep 4
	./ds-client -a ff
	sleep 2
	./ds-server -c $config -v brief > bf$i-brief.log&
	sleep 4
	./ds-client -a bf
	sleep 2
	./ds-server -c $config -v brief > wf$i-brief.log&
	sleep 4
	./ds-client -a wf
	sleep 2
	((i++))
	echo =============
done
