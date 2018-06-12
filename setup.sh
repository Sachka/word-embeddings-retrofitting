#!/bin/bash

### File and folder shortcuts ###
RES="resources"
EVAL="evaluation"
VECT="vectors"
PPDB="ppdb-1.0-xl-lexical.gz"
WOLF="wolf-1.0b4.xml.bz2"
FRV="vectors50.bz2"
ENV="vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2"
LXEV="lexical_similarity.zip"
SSA="stanford_sentiment_analysis.tar.gz"
BAR="============================================================================"

### Some colors ###
WHITE=`tput setaf 9`
RED=`tput setaf 1`
YELLOW=`tput setaf 3`
CYAN=`tput setaf 6`

### Error handling ###
DIROK=1
RESOK=1
VECOK=1
EVAOK=1


clear
echo "${WHITE}$BAR"
echo "Word2vec-Retrofitting Project Setup Script"
echo $BAR


### Creating folders ###
printf "${CYAN}$BAR\nSetting up directory structure.\n$BAR\n"
if [ ! -d ./$RES/ ] ; then
  mkdir -p $RES/ ;
  else
      echo "${YELLOW}$RES folder already exist."
      DIROK=0
fi

if [ ! -d ./$EVAL/ ] ; then
  mkdir -p $EVAL/ ;
else
  echo "$EVAL folder already exist."
  DIROK=0
fi

if [ ! -d ./$VECT/ ] ; then
  mkdir -p $VECT/ ;
else
  echo "$VECT folder already exist.${WHITE}"
  DIROK=0
fi

if [ $DIROK == 1 ] ; then
    echo "${WHITE}Directories created successfully."
else
    echo "${WHITE}Moving on..."
fi
sleep 1


### Semantic resources ###
printf "${CYAN}$BAR\nFetching semantic resources.\n$BAR\n"
if [ ! -f $PPDB ] ; then
    echo "${WHITE}Downloading $PPDB..."
    wget -nv http://www.cis.upenn.edu/%7Eccb/ppdb/release-1.0/ppdb-1.0-xl-lexical.gz >/dev/null 2>&1 ||
    curl -0 http://www.cis.upenn.edu/%7Eccb/ppdb/release-1.0/ppdb-1.0-xl-lexical.gz ;
else
    echo "${YELLOW}$PPDB already present."
    RESOK=0
fi

if [ ! -f $WOLF ] ; then
    echo "${WHITE}Downloading $WOLF..."
    wget -nv https://gforge.inria.fr/frs/download.php/file/33496/wolf-1.0b4.xml.bz2 >/dev/null 2>&1 ||
    curl -0 https://gforge.inria.fr/frs/download.php/file/33496/wolf-1.0b4.xml.bz2
else
    echo "${YELLOW}$WOLF already present."
    RESOK=0
fi

if [ $RESOK == 0 ] ; then
    echo "${WHITE}Moving on..."
fi
sleep 1
RESOK=1

### Word vectors ###
printf "${CYAN}$BAR\nFetching word vectors.\n$BAR\n"
if [ ! -f $FRV ] ; then
    echo "${WHITE}Downloading $FRV..."
    wget -nv https://dl.dropboxusercontent.com/u/108240016/vectors50.bz2 >/dev/null 2>&1 ||
    curl -0 https://dl.dropboxusercontent.com/u/108240016/vectors50.bz2
else
    printf "${YELLOW}$FRV already present.\n"
    RESOK=0
fi

if [ ! -f $ENV ] ; then
    echo "${WHITE}Downloading $ENV..."
    wget -nv https://dl.dropboxusercontent.com/u/108240016/vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2 >/dev/null 2>&1 ||
    curl -0 https://dl.dropboxusercontent.com/u/108240016/vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2
else
    printf "${YELLOW}$ENV already present.\n"
    RESOK=0
fi

if [ $RESOK == 0 ] ; then
    echo "${WHITE}Moving on..."
fi
sleep 1

### Evaluation files ###
printf "${CYAN}$BAR\nFetching evaluation files.\n$BAR\n"
if [ ! -f $LXEV ] ; then
    echo "${WHITE}Downloading $LXEV..."
    wget -nv https://dl.dropboxusercontent.com/s/zy176si8xm2eb40/lexical_similarity.zip >/dev/null 2>&1 ||
    curl -0 https://dl.dropboxusercontent.com/s/zy176si8xm2eb40/lexical_similarity.zip
else
    echo "${YELLOW}$LXEV already present."
    EVAOK=0
fi

if [ ! -f $SSA ] ; then
    echo "${WHITE}Downloading $SSA..."
    wget -nv https://dl.dropboxusercontent.com/s/zzgpqmrv1m9omyu/stanford_sentiment_analysis.tar.gz >/dev/null 2>&1 ||
    curl -0 https://dl.dropboxusercontent.com/s/zzgpqmrv1m9omyu/stanford_sentiment_analysis.tar.gz
else
    echo "${YELLOW}$SSA already present."
    EVAOK=0
fi

if [ $EVAOK == 0 ] ; then
    echo "${WHITE}Moving on..."
fi
sleep 1

### Unpacking packages ###
printf "${CYAN}$BAR\nUnpacking packages.\n$BAR\n"
echo "${WHITE}Unpacking $PPDB..."
gzip -dc < $PPDB > $RES/ppdb-1.0-xl-lexical
echo "${WHITE}Unpacking $WOLF..."
bzip2 -d < $WOLF > $RES/wolf-1.0b4.xml
echo "${WHITE}Unpacking $FRV..."
bzip2 -d < $FRV > $VECT/vectors50
echo "${WHITE}Unpacking $ENV..."
tar -xf vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2 -C $VECT/
echo "${WHITE}Unpacking $LXEV..."
unzip -qq -o lexical_similarity.zip -d $EVAL/
echo "${WHITE}Unpacking $SSA..."
tar -xf stanford_sentiment_analysis.tar.gz -C $EVAL/
sleep 1

#### Cleaning up ###
printf "${CYAN}$BAR\nCleaning up.\n$BAR\n"
rm -rf ppdb-1.0-xl-lexical.gz wolf-1.0b4.xml.bz2 vectors50.bz2 \
vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2 lexical_similarity.zip \
stanford_sentiment_analysis.tar.gz
echo "${WHITE}Done cleaning."
sleep 1
echo "${CYAN}End of script."
