#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

new_tab() {
  osascript -e "tell app \"Terminal\" \
      to do script \"cd $ROOT_DIR && $1\""
}

echo "Start"

new_tab "cd energy-community-usage-service      && mvn spring-boot:run"
sleep 5
new_tab "cd energy-community-percentage-service && mvn spring-boot:run"
sleep 3
new_tab "cd energy-community-rest-api           && mvn spring-boot:run"
sleep 3

new_tab "cd energy-community-producer           && mvn spring-boot:run"
new_tab "cd energy-community-user               && mvn spring-boot:run"
new_tab "cd energy-community-gui                && mvn exec:java -Dexec.mainClass=com.energy.community.GUI"

echo "✅  Все модули стартуют в отдельных вкладках…"
