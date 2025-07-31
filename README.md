# 📲 BLE-Presença - Peripheral Android

Este repositório contém o aplicativo Android que atua como **periférico BLE**, transmitindo a presença do aluno em sala de aula para ser detectada automaticamente por um dispositivo iOS (iPhone) do professor, que atua como **central BLE**.

---

## 🧩 Visão Geral do Sistema

| Papel     | Dispositivo | Função                  | Ação                          |
|-----------|-------------|-------------------------|-------------------------------|
| Professor | iPhone      | Central (Scanner BLE)   | Escaneia os alunos presentes  |
| Aluno     | Android     | Periférico (Advertiser) | Anuncia sua presença via BLE  |

---

## 📡 Funcionamento

1. O aluno abre o app Android.
2. O app solicita permissões e inicia a **publicidade BLE**, transmitindo um identificador único (ex: `aluno1234`) junto ao UUID da aula.
3. O iPhone do professor escaneia dispositivos próximos e detecta os anúncios emitidos por alunos, identificando quem está presente e sua proximidade (via RSSI).

> ⚠️ Testes realizados apenas para app em primeiro plano.

---

## 🧪 Arquivos Principais

- `MainActivity.kt` – Lógica de inicialização e gerenciamento do anúncio BLE.
- `AndroidManifest.xml` – Declaração de permissões necessárias para funcionamento BLE.
- `activity_main.xml` – Interface gráfica com botão para iniciar a publicidade.

---

## ⚙️ Setup Android

### Pré-requisitos

- [Android Studio](https://developer.android.com/studio) instalado
- JDK 17
- Gradle

Instalação via terminal (macOS):

```bash
brew install openjdk@17
brew install gradle
```

## 🔓 Ativando o modo desenvolvedor no celular Android

1. Abra **Configurações** no seu dispositivo Android  
2. Vá até **Sobre o telefone**  
3. Toque **7 vezes** em **"Número da versão"** até ver a mensagem _"Você agora é um desenvolvedor!"_  
4. Volte e entre no menu **Opções do desenvolvedor**  
5. Ative a opção **Depuração USB**

> ✅ Isso permitirá instalar e testar o app diretamente pelo Android Studio via cabo USB

