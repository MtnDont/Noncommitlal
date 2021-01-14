clc;                                               % Clears the screen
clear all;
nList = [10, 100, 1000, 10000];
n = 10;
f = @(t,y)((y^2)+(1/(t^2))); % Function f
fexact=@(x)((1/(2*x))*(sqrt(3)*tan((sqrt(3)/2)*(log(abs(x))))-1)); % Exact solution f
t0 = 1;
tn = 2;
y0 = -0.5;
[yapprox, err] = RK4MultN(f, fexact, t0, tn, y0, nList);

log_h = [];
for i=1:length(nList)
    log_h(i) = log(abs((tn-t0)/nList(i)));
end
log_err = log(err);
plot(log_h, log_err);
xlabel('log\_h');
ylabel('log\_err');
p = (log_err(2)-log_err(1))/(log_h(2)-log_h(1));
fprintf('%4.15f ',yapprox);

function [y, err] = RK4MultN(f, fexact, t0, tn, y0, nList)
    for i=1:size(nList,2)
        y(i) = RK4(f, t0, tn, y0, nList(i));
        err(i) = abs(fexact(tn) - y(i));
    end
end

function y1 = RK4(f, t0, tn, y0, n)
    h=(tn-t0)/n; % step size (smaller step size gives more accurate solutions)
    x = t0:h:tn; % x space
    y1=y0; % initial condition
    for i=1:(length(x)-1)
        y0 = y1;
        k1 = f(x(i),y0);
        k2 = f(x(i)+0.5*h,y0+0.5*h*k1);
        k3 = f((x(i)+0.5*h),(y0+0.5*h*k2));
        k4 = f((x(i)+h),(y0+k3*h));
        y1 = y0 + (1/6)*(k1+2*k2+2*k3+k4)*h; % main equation
    end
end