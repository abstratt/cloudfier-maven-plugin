#!/bin/bash
mvn jgitflow:release-start -B
mvn jgitflow:release-finish -DnoDeploy=true -DskipTests=true
git push --all --follow-tags
