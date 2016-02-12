[GlobalParams]
  model_type = 7
  gravity = '0 0 0'
  stabilization_type = 'LAPIDUS'
  initial_T = <fill me in>
  initial_p = <fill me in>
  initial_v = <fill me in>
  global_init_alpha = <fill me in>
  scaling_factor_2phase = '1e-3 1e-1 1e-5 1e-7 1e0 1e-3 1e-7'
  phase_interaction = true
  pressure_relaxation = true
  velocity_relaxation = true
  interface_transfer = true
  specific_interfacial_area_max_value = 1700
[]
[EoS]
  [./eos_liquid]
    type = StiffenedGasEquationOfStateLiquid
    gamma = 2.35
    q = -1167e3
    q_prime = 0
    p_inf = 1.e9
    cv = 1816
  [../]
  [./eos_vapor]
    type = StiffenedGasEquationOfStateVapor
    gamma = 1.43
    q = 2030e3
    q_prime = -23e3
    p_inf = 0
    cv = 1040
  [../]
[]
[Components]
  [./pipe1]
    type = Pipe
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
    position = '0 0 0'
    orientation = '1 0 0'
    A = <fill me in>
    f = <fill me in>
    Hw = 0.0
    Tw = <fill me in>
    length = 1
    n_elems = 100
  [../]
  [./br1]
    type = SimpleJunction
    inputs = 'pipe1(out)'
    outputs = 'pipe2(in)'
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
  [../]
  [./pipe2]
    type = Pipe
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
    position = '1 0 0'
    orientation = '1 0 0'
    A = <fill me in>
    f = <fill me in>
    Hw = <fill me in>
    Tw = <fill me in>
    length = 4
    n_elems = 100
  [../]
  [./br2]
    type = SimpleJunction
    inputs = 'pipe2(out)'
    outputs = 'pipe3(in)'
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
  [../]
  [./pipe3]
    type = Pipe
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
    position = '5 0 0'
    orientation = '1 0 0'
    A = <fill me in>
    f = <fill me in>
    Hw = 0.0
    Tw = <fill me in>
    length = 1
    n_elems = 100
  [../]
  [./inlet]
    type = Inlet
    input = 'pipe1(in)'
    rhou_liquid = <fill me in>
    H_liquid = <fill me in>
    rhou_vapor = <fill me in>
    H_vapor = <fill me in>
    alpha = <fill me in>
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
  [../]
  [./outlet]
    type = Outlet
    input = 'pipe3(out)'
    p_vapor = <fill me in>
    p_liquid = <fill me in>
    eos_liquid = eos_liquid
    eos_vapor = eos_vapor
  [../]
[]
[Preconditioning]
  active = 'SMP_PJFNK'
# active = 'SMP_Newton'
# active = 'FDP_Newton'
# active = 'FDP_PJFNK'
  [./SMP_Newton]
    type = SMP
    full = true
    solve_type = 'NEWTON'
  [../]
  [./SMP_PJFNK]
    type = SMP
    full = true
    solve_type = 'PJFNK'
    petsc_options_iname = '-mat_mffd_type'
    petsc_options_value = 'ds'
  [../]
  [./FDP_Newton]
    type = FDP
    full = true
    solve_type = 'NEWTON'
    petsc_options_iname = '-mat_fd_coloring_err'
    petsc_options_value = '1.e-10'
  [../]
  [./FDP_PJFNK]
    type = FDP
    full = true
    solve_type = 'PJFNK'
    petsc_options_iname = '-mat_fd_coloring_err -mat_fd_type -mat_mffd_type'
    petsc_options_value = '1.e-10 ds ds'
  [../]
[]
[Executioner]
  type = Transient
  dt = 1.e-5
  dtmin = 1.e-8
  petsc_options_iname = '-ksp_gmres_restart'
  petsc_options_value = '300'
  nl_rel_tol = 1e-13
  nl_abs_tol = 1e-9
# nl_abs_step_tol = 1e-15
  nl_max_its = 30
  l_tol = 1e-3
  l_max_its = 100
  start_time = 0.0
  num_steps = 20
  [./Quadrature]
    type = TRAP
    order = FIRST
  [../]
[]
[Output]
  output_displaced = true
  perf_log = true
[]
