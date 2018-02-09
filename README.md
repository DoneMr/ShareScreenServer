# Custom video protocol by Done
---
屏幕分享服务端，录制屏幕，h264方式编码，通过UDP方式推送到客户端

request structure
METHOD CSeq length Content CRLF

response structure
STATUS CSeq length Content CRLF

METHOD Lists
* SETUP      //operate transport param such as client control port/TCP and video port/UDP
* PLAY       //start transfer video for phone window
* TEARDOWN   //stop transmission
* HEART      //heart package for keeping communicating
* HOME       //mock clicking home key
* BACK       //mock clicking back key
* MENU       //mock clicking menu key
* VOLUME     //mock clicking menu key
* CLICK      //mock clicking window
* TOUCH      //mock touching window

control port is Odd Number such as PORT%2 = 1

video port is Even Number such as PORT%2 = 0
```des




sample:

/******************** C->S **********************/
//                                               /
//   METHOD                                      /
//     |                                         /
//     |  CSeq                                   /
//     |   |                                     /
//     |   |Length                               /
//     |   |  |                                  /
//     |   |  |           Content                /
//     |   |  |              |                   /
//     |   |  |              |     CRLF          /
//     |   |  |              |      |            /
//     |   |  |              |      |            /
//   SETUP 1 17 client-port=60001 \r\n           /
//                                               /
/************************************************/




/********************************* S->C *********************************/
/                                                                        /
/    STATUS                                                              /
/      | CSeq                                                            /
/      |  |Length                                                        /
/      |  |  |                  Content                                  /
/      |  |  |                     |              CRLF                   /
/      |  |  |                     |               |                     /
/      |  |  |                     |               |                     /
/      |  |  |                     |               |                     /
/      |  |  |                     |               |                     /
/      |  |  | ---------------------------------   |                     /
/     200 1 33 client-port=60001;server-port=999 \r\n                    /
/                                                                        /
/************************************************************************/




/****************** C->S *****************/
//                                        /
//   METHOD                               /
//     |                                  /
//     |  CSeq                            /
//     |   |                              /
//     |   |Length                        /
//     |   |  |                           /
//     |   |  |      Content              /
//     |   |  |        |                  /
//     |   |  |        |           CRLF   /
//     |   |  |        |            |     /
//     |   |  |        |            |     /
//  VOLUME 1 17 set=up;type=alarm \r\n    /
//                                        /
/*****************************************/

/********************* S->C ******************/
/                                             /
/    STATUS                                   /
/      | CSeq                                 /
/      |  |Length                             /
/      |  | |   Content                       /
/      |  | |     |    CRLF                   /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | | --------  |                     /
/     200 1 8 alarm=ok \r\n                   /
/                                             /
/*********************************************/




/********************** C->S **********************/
//                                                 /
//   METHOD                                        /
//     |                                           /
//     |  CSeq                                     /
//     |   |                                       /
//     |   |Length                                 /
//     |   | |                                     /
//     |   | |    Content                          /
//     |   | |      |                              /
//     |   | |      |                      CRLF    /
//     |   | |      |                       |      /
//     |   | |      |                       |      /
//   CLICK 1 8 src=1280,720;order=200,250 \r\n     /
//                                                 /
/**************************************************/

/********************* S->C ******************/
/                                             /
/    STATUS                                   /
/      | CSeq                                 /
/      |  |Length                             /
/      |  | |   Content                       /
/      |  | |     |    CRLF                   /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | | --------  |                     /
/     200 1 8 click=ok \r\n                   /
/                                             /
/*********************************************/


/************************** C->S **************************/
//                                                         /
//   METHOD                                                /
//     |                                                   /
//     |  CSeq                                             /
//     |   |                                               /
//     |   |Length                                         /
//     |   | |                                             /
//     |   | |            Content                          /
//     |   | |              |                              /
//     |   | |              |                      CRLF    /
//     |   | |              |                       |      /
//     |   | |              |                       |      /
//   TOUCH 1 8 src=1280,720;order=200,250,200,300 \r\n     /
//                                                         /
/**********************************************************/

/********************* S->C ******************/
/                                             /
/    STATUS                                   /
/      | CSeq                                 /
/      |  |Length                             /
/      |  | |   Content                       /
/      |  | |     |    CRLF                   /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | |     |     |                     /
/      |  | | --------  |                     /
/     200 1 8 touch=ok \r\n                   /
/                                             /
/*********************************************/


```
