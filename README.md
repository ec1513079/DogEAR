�y�K�v�v�f�z
�EOpenSSH
�Egit-client
�Emake
�Eandroid-ndk
�Eandroid-sdk

git@gitlab.mobilegate.biz:kotakahashi/scaforandroid.git ����Pull

mupdf���擾
$ git submodule update --init

�܂���mupdf���C�u�������Z�b�g�A�b�v

mupdf�̃T�u���W���[�����擾
$ cd mupdf
/mupdf $ git submodule update --init

make�Ńr���h�ɕK�v�ȃf�B���N�g���ƃt�@�C�����쐬
/mupdf $ make generate

ndk�r���h
�i�^�[�~�i���ł��ł��邪�A�ŋ߂�ADT�iADTr20�j���g���΁A
�@CDT (C/C++ Development Tools) �����Ă���̂ŁAEclipse��ł��r���h�ł���j

�F�^�[�~�i���Ńr���h
/mupdf $ cd android
/mupdf/android $ cp local.properties.sample local.properties
/mupdf/android $ ndk-build

�FEclipse�Ńr���h
Eclipse��/mupdf/android �̃v���W�F�N�g���J��
�v���W�F�N�g���E�N���b�N - [Android �c�[��] - [Add Native Support ...] �����s���āA
�v���W�F�N�g�� NDK ���T�|�[�g������B
�����r���hON�Ȃ炻�̂܂܃r���h�����͂��B

�� C�t�@�C���̃C���N���[�h�ŃG���[���o��ꍇ
�v���W�F�N�g�̃v���p�e�B - C/C++ ��� �] �p�X����уV���{�� - �C���N���[�h �ŁA
"C:\android-ndk\platforms\android-14\arch-arm\usr\include"
"C:\Users\kotakahashi\Documents\workspace\mupdf\fitz"
���C���N���[�h�p�X�ɒǉ��B

�ySCAforAndroid���r���h�z

�܂���mupdf�����C�u�����Ƃ��ēo�^����B

1. mupdf��android�v���W�F�N�g�̃v���p�e�B - Android - ���C�u�����p�l����
�@�@�u���C�u�����v�`�F�b�N�{�b�N�X��ON
2. SCA�v���W�F�N�g�̃v���p�e�B - Android - ���C�u�����p�l����
�@�@mupdf���Q�ƃ��C�u�����Ƃ��ēo�^�B

����Ńr���h���ʂ�͂��B