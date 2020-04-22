public class EvaluationFunctionWinLose implements IEvaluationFunction {

    @Override
    public int evalueStatus(Board b) {
//        b.displayBoard();
        int scores = 0;
        if (b.fields[0][0] == 'x')
            scores += 100;
        if(b.fields[0][1] == 'o')
            scores += 31;
        if (b.fields[0][1] == 'x')
            scores += 200;
        if(b.fields[0][3] == 'o')
            scores += 59;

        System.out.println("Scores: "+scores);
        return scores;

    }
}

//        int aiScore=1;
//        int score=0;
//        int blanks = 0;
//        int k=0, moreMoves=0;
//        for(int i=5;i>=0;--i){
//            for(int j=0;j<=6;++j){
//
////                if(b.fields[i][j]==0) continue;
//
//                if(j<=3){
//                    for(k=1;k<4;++k){
//                        if(b.fields[i][j+k]=='x')aiScore++;
//                        else if(b.fields[i][j+k]=='o'){aiScore=0;blanks = 0;break;}
//                        else blanks++;
//                    }
//
//                    moreMoves = 0;
//                    if(blanks>0)
//                        for(int c=1;c<4;++c){
//                            int column = j+c;
//                            for(int m=i; m<= 5;m++){
//                                if(b.fields[m][column]==' ')moreMoves++;
//                                else break;
//                            }
//                        }
//
//                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
//                    aiScore=1;
//                    blanks = 0;
//                }
//
//                if(i>=3){
//                    for(k=1;k<4;++k){
//                        if(b.fields[i-k][j]=='x')aiScore++;
//                        else if(b.fields[i-k][j]=='o'){aiScore=0;break;}
//                    }
//                    moreMoves = 0;
//
//                    if(aiScore>0){
//                        int column = j;
//                        for(int m=i-k+1; m<=i-1;m++){
//                            if(b.fields[m][column]==' ')moreMoves++;
//                            else break;
//                        }
//                    }
//                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
//                    aiScore=1;
//                    blanks = 0;
//                }
//
//                if(j>=3){
//                    for(k=1;k<4;++k){
//                        if(b.fields[i][j-k]=='x')aiScore++;
//                        else if(b.fields[i][j-k]=='o'){aiScore=0; blanks=0;break;}
//                        else blanks++;
//                    }
//                    moreMoves=0;
//                    if(blanks>0)
//                        for(int c=1;c<4;++c){
//                            int column = j- c;
//                            for(int m=i; m<= 5;m++){
//                                if(b.fields[m][column]==' ')moreMoves++;
//                                else break;
//                            }
//                        }
//
//                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
//                    aiScore=1;
//                    blanks = 0;
//                }
//
//                if(j<=3 && i>=3){
//                    for(k=1;k<4;++k){
//                        if(b.fields[i-k][j+k]=='x')aiScore++;
//                        else if(b.fields[i-k][j+k]=='o'){aiScore=0;blanks=0;break;}
//                        else blanks++;
//                    }
//                    moreMoves=0;
//                    if(blanks>0){
//                        for(int c=1;c<4;++c){
//                            int column = j+c, row = i-c;
//                            for(int m=row;m<=5;++m){
//                                if(b.fields[m][column]==' ')moreMoves++;
//                                else if(b.fields[m][column]=='x');
//                                else break;
//                            }
//                        }
//                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
//                        aiScore=1;
//                        blanks = 0;
//                    }
//                }
//
//                if(i>=3 && j>=3){
//                    for(k=1;k<4;++k){
//                        if(b.fields[i-k][j-k]=='x')aiScore++;
//                        else if(b.fields[i-k][j-k]=='o'){aiScore=0;blanks=0;break;}
//                        else blanks++;
//                    }
//                    moreMoves=0;
//                    if(blanks>0){
//                        for(int c=1;c<4;++c){
//                            int column = j-c, row = i-c;
//                            for(int m=row;m<=5;++m){
//                                if(b.fields[m][column]==' ')moreMoves++;
//                                else if(b.fields[m][column]=='x');
//                                else break;
//                            }
//                        }
//                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
//                        aiScore=1;
//                        blanks = 0;
//                    }
//                }
//            }
//        }
//        return score;
//    }
//
//    int calculateScore(int aiScore, int moreMoves){
////        System.out.println("AiScore: "+ aiScore + "MoreMoves: " + moreMoves);
//        int moveScore = 4 - moreMoves;
//        if(aiScore==0)return 0;
//        else if(aiScore==1)return 1*moveScore;
//        else if(aiScore==2)return 10*moveScore;
//        else if(aiScore==3)return 100*moveScore;
//        else return 1000;
//    }
//}
