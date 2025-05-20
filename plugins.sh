#!/bin/bash

# CONFIGURATION
JENKINS_URL="http://jenkins.lab.pl/"
JENKINS_USER="admin"
JENKINS_API_TOKEN="jenkins123"
 
#Plugins backup
#mv -v ./config/plugins.txt ./config/plugins.txt.bak-$(date +'%Y%m%d%H%M%S')

# Get local plugin versions
echo "[INFO] Getting list of installed plugins..."
#plugins=$(curl -s -u "$JENKINS_USER:$JENKINS_API_TOKEN" "$JENKINS_URL/pluginManager/api/json?depth=1" \
#    | jq -r '.plugins[] | "\(.shortName):\(.version)"')
plugins=$(cat ./config/plugins.txt)

echo "[INFO] Persist local plugins to file..."
echo "$plugins" > ./config/plugins.txt

echo "[INFO] Getting data from Jenkins Update Center..."
update_center=$(curl -s https://archives.jenkins.io/update-center/current/update-center.actual.json \
    | jq -r '.plugins | to_entries[] | "\(.key):\(.value.version)"')

echo ""
echo "📦 Plugins to update:"
echo "---------------------------"

updates_found=0

# Compare local versions with the latest versions
while IFS=":" read -r name local_version; do
    latest_version=$(echo "$update_center" | awk -F':' -v name="$name" '$1 == name { print $2 }')

    if [[ -z "$latest_version" ]]; then
        echo "⚠️  No data for plugin: $name"
        continue
    fi

    if [[ "$local_version" != "$latest_version" ]]; then
        sed -i "/^$name:/d" ./config/plugins.txt
        echo "🔄 $name: $local_version → $latest_version"
        echo "$name:$latest_version" >> ./config/plugins.txt
        ((updates_found++))
    fi
done <<< "$plugins" 

if [[ "$updates_found" -eq 0 ]]; then
    echo "✅ All plugins are up to date."
fi

