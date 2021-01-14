clc;
clear all;
f = @(t,y)((y^2)+(1/(t^2))); % Derivative function
fexact=@(x)((1/(2*x))*(sqrt(3)*tan((sqrt(3)/2)*(log(abs(x))))-1)); % Exact function
t0 = 1; 
tn = 2;
y0 = -0.5;
n = [10,100,1000,10000]; % List of steps

% Find y_approx(2) for each n number of steps alongside their errors
[yapprox, err] = EulerMultN(f, fexact, t0, tn, y0, n);
log_h = [];
for i=1:length(n)
    log_h(i) = log((tn-t0)/n(i));
end
log_err = log(err);
% Plot log_err vs log_h
plot(log_h, log_err);
xlabel('log\_h');
ylabel('log\_err');
% Find slope of the line
p = polyfit(log_h, log_err, 1);
fprintf('%4.15f ',yapprox);

function [y, err] = EulerMultN(f, fexact, t0, tn, y0, nList)
    for i=1:size(nList,2)
        y(i) = EulerFunction(f, t0, tn, y0, nList(i));
        err(i) = abs(fexact(tn) - y(i));
    end
end

function x = EulerFunction(f, t0, tn, y0, n)
    h=(tn-t0)/n;
    while t0<=tn
        y1=y0+h*f(t0,y0);
        t1=t0+h;
        t0=t1;
        y0=y1;  
    end
    x = y0; %Solution of y(tn)
end