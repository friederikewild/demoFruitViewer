# use curl to download a keystore from $KEYSTORE_URI, if set,
# to the path/filename set in $KEYSTORE.
if [[ $KEYSTORE && ${KEYSTORE} && $KEYSTORE_URI && ${KEYSTORE_URI} ]]
then
    echo "Keystore detected - downloading..."
    curl -L -o ${KEYSTORE} ${KEYSTORE_URI}
else
    if [[ $KEYSTORE && ${KEYSTORE} ]]
    then
        echo "Keystore not set.  .APK artifact will not be signed."
    else
        echo "Keystore uri not set.  .APK artifact will not be signed."
    fi
fi