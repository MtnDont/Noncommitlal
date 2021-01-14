clc;
clear all;
f = @(t,y)((y^2)+(1/(t^2))); % Derivative function
dfdt = @(t,y)(2*y^3 + (2*y/(t^2)) - 2/(t^3)); % 2nd Derivative function
d2fdt2 = @(t,y)(6*y^4 + ((8*y^2)/(t^2)) - ((4*y)/(t^3)) + (8/(t^4))); % 3rd Derivative function
fexact=@(x)((1/(2*x))*(sqrt(3)*tan((sqrt(3)/2)*(log(abs(x))))-1)); % Exact function
t0 = 1;
tn = 2;
y0 = -0.5;
n = [10,100,1000,10000]; % List of steps

% Find y_approx(2) for each n number of steps alongside their errors
[yapprox, err] = TaylorMultN(f, dfdt, d2fdt2, fexact, t0, tn, y0, n);
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
p = (log_err(2)-log_err(1))/(log_h(2)-log_h(1));
fprintf('%4.15f ',yapprox);

function [y, err] = TaylorMultN(f, dfdt, d2fdt2, fexact, t0, tn, y0, nList)
    for i=1:size(nList,2)
        y(i) = TaylorFunction(f, dfdt, d2fdt2, t0, tn, y0, nList(i));
        err(i) = abs(fexact(tn) - y(i));
    end
end

function x = TaylorFunction(f, dfdt, d2fdt2, t0, tn, y0, n)
    h=(tn-t0)/n;
    while t0<=tn
        y1 = y0 + h*f(t0,y0) + (h^2/2)*dfdt(t0,y0) + (h^3/3)*d2fdt2(t0,y0);
        t1=t0+h;
        t0=t1;
        y0=y1;  
    end
    x = y0; % Solution of y(tn)
end