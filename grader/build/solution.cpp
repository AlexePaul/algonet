#include <iostream>
#include <fstream>
using namespace std;
ifstream fin("bac.txt");
int main()
{
    int n,apar_maxim=0,ultim_nr=0;
    fin>>ultim_nr;
    apar_maxim++;
    while(fin>>n)
    {
        if(n==ultim_nr)
        {
            apar_maxim++;
        }
        else
        {

            cout<<ultim_nr<<' '<<apar_maxim<<' ';
             ultim_nr=n;
            apar_maxim=1;
        }
    }
    cout<<ultim_nr<<' '<<apar_maxim;
}